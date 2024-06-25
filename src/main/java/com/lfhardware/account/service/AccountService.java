package com.lfhardware.account.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lfhardware.account.dto.ResetPasswordInput;
import com.lfhardware.account.dto.UserCredentialsDTO;
import com.lfhardware.account.exception.InvalidPasswordException;
import com.lfhardware.account.mapper.CredentialMapper;
import com.lfhardware.address.mapper.AddressMapper;
import com.lfhardware.auth.domain.Address;
import com.lfhardware.auth.domain.Profile;
import com.lfhardware.auth.dto.*;
import com.lfhardware.auth.mapper.RoleMapper;
import com.lfhardware.auth.mapper.UserRecordMapper;
import com.lfhardware.customer.repository.ICustomerRepository;
import com.lfhardware.configuration.KeycloakProperties;
import com.lfhardware.keycloak.rest.account.KeycloakAccountService;
import com.lfhardware.keycloak.rest.account.dto.OTPQrCodeDTO;
import com.lfhardware.keycloak.rest.account.dto.OtpDTO;
import com.stripe.StripeClient;
import com.stripe.model.Account;
import com.stripe.model.AccountLink;
import com.stripe.param.AccountCreateParams;
import com.stripe.param.AccountLinkCreateParams;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.core.Response;
import org.hibernate.reactive.stage.Stage;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.ErrorRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class AccountService implements IAccountService {

    private final StripeClient stripeClient;

    private final Keycloak keycloak;

    private final UserRecordMapper userRecordMapper;

    private final CredentialMapper credentialMapper;

    private final ObjectMapper objectMapper;

    private final AddressMapper addressMapper;

    private final RoleMapper roleMapper;

    private KeycloakAccountService keycloakAccountService;

    private final KeycloakProperties keycloakProperties;

    private final Stage.SessionFactory sessionFactory;

    private final ICustomerRepository customerRepository;

    public AccountService(StripeClient stripeClient,
                          Keycloak keycloak,
                          UserRecordMapper userRecordMapper,
                          ObjectMapper objectMapper,
                          AddressMapper addressMapper,
                          CredentialMapper credentialMapper,
                          RoleMapper roleMapper,
                          KeycloakAccountService keycloakAccountService,
                          KeycloakProperties keycloakProperties,
                          Stage.SessionFactory sessionFactory,
                          ICustomerRepository customerRepository) {
        this.stripeClient = stripeClient;
        this.keycloak = keycloak;
        this.userRecordMapper = userRecordMapper;
        this.objectMapper = objectMapper;
        this.addressMapper = addressMapper;
        this.credentialMapper = credentialMapper;
        this.roleMapper = roleMapper;
        this.keycloakAccountService = keycloakAccountService;
        this.keycloakProperties = keycloakProperties;
        this.sessionFactory = sessionFactory;
        this.customerRepository = customerRepository;
    }

    @Override
    public Mono<UserDTO> findCurrentlyLoggedInUserAccount() {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .flatMap(authentication -> {
                    return Mono.fromCallable(() -> {
                                UserRepresentation userRepresentation = keycloak.realm(keycloakProperties.getRealm())
                                        .users()
                                        .get(authentication.getName())
                                        .toRepresentation(true);
                                UserDTO userDTO = userRecordMapper.mapToUserDTO(userRepresentation);
                                Map<String, List<String>> userAttributes = userRepresentation.getAttributes();

                                userDTO.setFirstTimeLogin(keycloak.realm(keycloakProperties.getRealm())
                                        .getEvents()
                                        .stream()
                                        .filter(event -> event.getType()
                                                .equals("LOGIN") && event.getUserId()
                                                .equals(authentication.getName()))
                                        .count() == 1);

//                                List<String> phoneNumber = new ArrayList<>();
//                                List<String> addresses = new ArrayList<>();
//                                List<AddressDTO> addressDTOs = new ArrayList<>();
//
//                                if (Objects.nonNull(userAttributes) && Objects.nonNull(userAttributes.get("address"))) {
//                                    addresses = userAttributes.get("address");
//                                    if (!addresses.isEmpty()) {
//                                        addressDTOs = addresses.stream()
//                                                .map(address -> {
//                                                    try {
//                                                        return objectMapper.readValue(address, AddressDTO.class);
//                                                    } catch (JsonProcessingException e) {
//                                                        throw new RuntimeException(e);
//                                                    }
//                                                })
//                                                .toList();
//                                    }
//                                }
//                                if (Objects.nonNull(userAttributes) && CollectionUtils.isNotEmpty(userAttributes.get("phone_number"))) {
//                                    phoneNumber = userAttributes.get("phone_number");
//                                }
                                userDTO.setRoles(authentication.getAuthorities()
                                        .stream()
                                        .map(roleMapper::mapToRoleDTO)
                                        .collect(Collectors.toSet()));
                                userDTO.setEmailVerified(userRepresentation.isEmailVerified());
                                userDTO.setProfile(ProfileDTO.builder()
                                        .emailAddress(userRepresentation.getEmail())
                                        .build());
                                return userDTO;
                            })
                            .flatMap(userDTO -> {


                                boolean isCustomer = userDTO.getRoles()
                                        .stream()
                                        .anyMatch(role -> role.getName()
                                                .equals(Role.customer.toString()));
                                if (isCustomer) {
                                    return Mono.fromCompletionStage(sessionFactory.withSession(session -> customerRepository.findById(session, authentication.getName())
                                            .thenApply(customer -> {
                                                ProfileDTO profile = userDTO.getProfile();
                                                profile.setPhoneNumber(customer.getPhoneNumber());
                                                AddressDTO addressDTO = addressMapper.mapToAddressDTO(customer.getAddress());
                                                profile.setAddress(addressDTO);
                                                return userDTO;
                                            })));
                                }
                                return Mono.just(userDTO);
                            });
//                    return Mono.empty();
                });
    }

    @Override
    public Mono<Account> findStripeAccount(String accountId) {
        return Mono.fromCallable(() -> stripeClient.accounts()
                .retrieve(accountId));
    }

    @Override
    public Mono<Account> createStripeAccount() {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .flatMap(authentication -> {
                    return findById(authentication.getName())
                            .flatMap(user -> {
                                return Mono.fromCallable(() -> {
                                    AccountCreateParams accountCreateParams = AccountCreateParams.builder()
                                            .setEmail(user.getEmail())
                                            .setType(AccountCreateParams.Type.STANDARD)
                                            .setCountry("MY")
                                            .setMetadata(Map.of("user_id", authentication.getName()))
                                            .build();

                                    Account account = stripeClient.accounts()
                                            .create(accountCreateParams);

                                    return account;
                                });
                            });
                });
    }

    @Override
    public Mono<String> createStripeAccountOnboardingLink(String accountId) {
        return Mono.fromCallable(() -> {
            AccountLinkCreateParams accountLinkCreateParams =
                    AccountLinkCreateParams.builder()
                            .setAccount(accountId)
                            .setRefreshUrl("https://example.com/reauth")
                            .setReturnUrl("http://localhost:8090/user-onboarding/service-provider/completed")
                            .setType(AccountLinkCreateParams.Type.ACCOUNT_ONBOARDING)
                            .build();

            AccountLink accountLink = stripeClient.accountLinks()
                    .create(accountLinkCreateParams);
            return accountLink.getUrl();
        });
    }
//    @Override
//    public Mono<String> createStripeAccount() {
//
//        return ReactiveSecurityContextHolder.getContext()
//                .map(SecurityContext::getAuthentication)
//                .flatMap(authentication -> {
//                    return findById(authentication.getName())
//                            .flatMap(user -> {
//                                return Mono.fromCallable(() -> {
//                                            AccountCreateParams accountCreateParams = AccountCreateParams.builder()
//                                                    .setEmail(user.getEmail())
//                                                    .setType(AccountCreateParams.Type.STANDARD)
//                                                    .setCountry("MY")
//                                                    .setMetadata(Map.of("user_id", authentication.getName()))
//                                                    .build();
//
//                                            Account account = stripeClient.accounts()
//                                                    .create(accountCreateParams);
//
//                                            AccountLinkCreateParams accountLinkCreateParams =
//                                                    AccountLinkCreateParams.builder()
//                                                            .setAccount(account.getId())
//                                                            .setRefreshUrl("https://example.com/reauth")
//                                                            .setReturnUrl("http://localhost:8090/user-onboarding/service-provider/completed")
//                                                            .setType(AccountLinkCreateParams.Type.ACCOUNT_ONBOARDING)
//                                                            .build();
//
//                                            AccountLink accountLink = stripeClient.accountLinks()
//                                                    .create(accountLinkCreateParams);
//                                            return accountLink.getUrl();
//                                        })
//                                        .subscribeOn(Schedulers.boundedElastic());
//                            });
//                });
//
//        // keycloak.realm("LFHardware").users().get("a").setCredentialUserLabel();
//    }

    @Override
    public Mono<UserRepresentation> findById(String id) {
        return Mono.fromCallable(() -> keycloak.realm(this.keycloakProperties.getRealm())
                .users()
                .get(id)
                .toRepresentation());
    }

    @Override
    public Mono<Void> resetPassword(ResetPasswordInput resetPasswordInput) {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .flatMap(authentication -> {
                    return Mono.fromCallable(() -> {
                                try {
                                    UserResource userResource = keycloak.realm(keycloakProperties.getRealm())
                                            .users()
                                            .get(authentication.getName());
                                    CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
                                    credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
                                    credentialRepresentation.setValue(resetPasswordInput.getPassword());
                                    userResource.resetPassword(credentialRepresentation);
                                    return null;
                                } catch (ClientErrorException clientErrorException) {
                                    throw objectMapper.readValue(getErrorMessage(clientErrorException.getResponse()), InvalidPasswordException.class);
                                }
                            })
                            .subscribeOn(Schedulers.boundedElastic());
                })
                .then();
    }

    public Mono<OTPQrCodeDTO> generateOtpQrCode() {
        return ReactiveSecurityContextHolder.getContext()
                .map(securityContext -> (Jwt) securityContext.getAuthentication()
                        .getCredentials())
                .flatMap(jwt -> {
                    return keycloakAccountService.generateOtpQrCode(jwt.getTokenValue());
                });
    }

    public Mono<Void> setupOTPVerification(OtpDTO otpDTO) {
        return ReactiveSecurityContextHolder.getContext()
                .map(securityContext -> (Jwt) securityContext.getAuthentication()
                        .getCredentials())
                .flatMap(jwt -> keycloakAccountService.setupOTPVerification(jwt.getTokenValue(), otpDTO));
    }

    public Mono<List<UserCredentialsDTO>> findAllUserCredentials() {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .flatMap(authentication ->
                        Mono.fromCallable(() -> keycloak.realm(keycloakProperties.getRealm())
                                .users()
                                .get(authentication.getName())
                                .credentials()
                                .stream()
                                .map(credentialMapper::mapToUserCredentialsDTO)
                                .collect(Collectors.toList())));
    }

    @Override
    public Mono<Void> updateCurrentlyLoggedInUserAccount(UserProfileDTO userProfileDTO) {
//        return Mono.fromCompletionStage(sessionFactory.withTransaction((session, transaction) -> userRepository.findByEmailAddress(session, username)
//                .thenCompose(user -> {
//
//                    AddressDTO addressDTO = userProfileDTO.getProfile()
//                            .getAddress();
//                    ProfileDTO profileDTO = userProfileDTO.getProfile();
//                    Profile profile = new Profile();
//
//                    if (Objects.nonNull(profile)) {
//                        profile.setPhoneNumber(profileDTO.getPhoneNumber());
//                        profile.setEmailAddress(profileDTO.getEmailAddress());
//
//                        if (Objects.nonNull(addressDTO)) {
//                            Address address = new Address();
//                            address.setAddressLine1(address.getAddressLine1());
//                            //profile.setAddress(new Address(addressDTO.getAddressLine1(), addressDTO.getAddressLine2(), addressDTO.getState(), addressDTO.getCity(), addressDTO.getZipcode()));
//                        }
//                    }
//                    user.setProfile(profile);
//
//                    return acc.save(session, user);
//                })));
        return Mono.empty();
    }

    public Mono<Void> updateAccountRoleById(String id, List<RoleRepresentation> roleRepresentations) {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .flatMap(authentication -> {
                    return Mono.fromRunnable(() -> {
                        keycloak.realm(this.keycloakProperties.getRealm())
                                .users()
                                .get(id)
                                .roles()
                                .realmLevel()
                                .add(roleRepresentations);
                    });
                });
    }


    private String getErrorMessage(Response response) {
        Object entity = response.getEntity();
        String errorMessage = "(none)";
        if (entity instanceof ErrorRepresentation)
            errorMessage = ((ErrorRepresentation) entity).getErrorMessage();
        else if (entity instanceof InputStream)
            errorMessage = new BufferedReader(new InputStreamReader((InputStream) entity)).lines()
                    .collect(Collectors.joining("\n"));
        else if (entity != null)
            errorMessage = entity.toString();
        return errorMessage;
    }
}
