package com.lfhardware.account.service;

import com.lfhardware.account.dto.ResetPasswordInput;
import com.lfhardware.account.dto.UserCredentialsDTO;
import com.lfhardware.auth.dto.UserProfileDTO;
import com.lfhardware.keycloak.rest.account.dto.OTPQrCodeDTO;
import com.lfhardware.keycloak.rest.account.dto.OtpDTO;
import com.lfhardware.user.dto.UserDTO;
import com.stripe.model.Account;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class AccountService implements IAccountService {
    @Override
    public Mono<UserDTO> findCurrentlyLoggedInUserAccount() {
        return null;
    }

    @Override
    public Mono<Account> findStripeAccount(String accountId) {
        return null;
    }

    @Override
    public Mono<UserRepresentation> findById(String id) {
        return null;
    }

    @Override
    public Mono<List<UserCredentialsDTO>> findAllUserCredentials() {
        return null;
    }

    @Override
    public Mono<Void> resetPassword(ResetPasswordInput resetPasswordInput) {
        return null;
    }

    @Override
    public Mono<OTPQrCodeDTO> generateOtpQrCode() {
        return null;
    }

    @Override
    public Mono<Void> setupOTPVerification(OtpDTO otpDTO) {
        return null;
    }

    @Override
    public Mono<Account> createStripeAccount() {
        return null;
    }

    @Override
    public Mono<String> createStripeAccountOnboardingLink(String accountId) {
        return null;
    }

    @Override
    public Mono<Void> updateCurrentlyLoggedInUserAccount(UserProfileDTO userProfileDTO) {
        return null;
    }

    @Override
    public Mono<Void> updateAccountRoleById(String id, List<RoleRepresentation> roleRepresentations) {
        return null;
    }
//
//    private final StripeClient stripeClient;
//
//    private final UserRecordMapper userRecordMapper;
//
//    private final CredentialMapper credentialMapper;
//
//    private final ObjectMapper objectMapper;
//
//    private final AddressMapper addressMapper;
//
//    private final RoleMapper roleMapper;
//
//    private KeycloakAccountService keycloakAccountService;
//
//    private final KeycloakProperties keycloakProperties;
//
//    private final Stage.SessionFactory sessionFactory;
//
//    private final ICustomerRepository customerRepository;
//
//    public AccountService(StripeClient stripeClient,
//                          UserRecordMapper userRecordMapper,
//                          ObjectMapper objectMapper,
//                          AddressMapper addressMapper,
//                          CredentialMapper credentialMapper,
//                          RoleMapper roleMapper,
//                          KeycloakAccountService keycloakAccountService,
//                          KeycloakProperties keycloakProperties,
//                          Stage.SessionFactory sessionFactory,
//                          ICustomerRepository customerRepository) {
//        this.stripeClient = stripeClient;
//        this.userRecordMapper = userRecordMapper;
//        this.objectMapper = objectMapper;
//        this.addressMapper = addressMapper;
//        this.credentialMapper = credentialMapper;
//        this.roleMapper = roleMapper;
//        this.keycloakAccountService = keycloakAccountService;
//        this.keycloakProperties = keycloakProperties;
//        this.sessionFactory = sessionFactory;
//        this.customerRepository = customerRepository;
//    }
//
//    @Override
//    public Mono<UserDTO> findCurrentlyLoggedInUserAccount() {
//        return Mono.empty();
////        return ReactiveSecurityContextHolder.getContext()
////                .map(SecurityContext::getAuthentication)
////                .flatMap(authentication -> {
////                    return Mono.fromCallable(() -> {
////                                UserRepresentation userRepresentation = keycloak.realm(keycloakProperties.getRealm())
////                                        .users()
////                                        .get(authentication.getName())
////                                        .toRepresentation(true);
////                                UserDTO userDTO = userRecordMapper.mapToUserDTO(userRepresentation);
////                                Map<String, List<String>> userAttributes = userRepresentation.getAttributes();
////
////                                userDTO.setFirstTimeLogin(keycloak.realm(keycloakProperties.getRealm())
////                                        .getEvents()
////                                        .stream()
////                                        .filter(event -> event.getType()
////                                                .equals("LOGIN") && event.getUserId()
////                                                .equals(authentication.getName()))
////                                        .count() == 1);
////                                userDTO.setRoles(authentication.getAuthorities()
////                                        .stream()
////                                        .map(roleMapper::mapToRoleDTO)
////                                        .collect(Collectors.toSet()));
////                                userDTO.setEmailVerified(userRepresentation.isEmailVerified());
////                                userDTO.setProfile(ProfileDTO.builder()
////                                        .emailAddress(userRepresentation.getEmail())
////                                        .build());
////                                return userDTO;
////                            })
////                            .flatMap(userDTO -> {
////
////
////                                boolean isCustomer = userDTO.getRoles()
////                                        .stream()
////                                        .anyMatch(role -> role.getName()
////                                                .equals(Role.customer.toString()));
////                                if (isCustomer) {
////                                    return Mono.fromCompletionStage(sessionFactory.withSession(session -> customerRepository.findById(session, authentication.getName())
////                                            .thenApply(customer -> {
////                                                ProfileDTO profile = userDTO.getProfile();
////                                                profile.setPhoneNumber(customer.getPhoneNumber());
////                                                AddressDTO addressDTO = addressMapper.mapToAddressDTO(customer.getAddress());
////                                                profile.setAddress(addressDTO);
////                                                return userDTO;
////                                            })));
////                                }
////                                return Mono.just(userDTO);
////                            });
//////                    return Mono.empty();
////                });
//    }
//
//    @Override
//    public Mono<Account> findStripeAccount(String accountId) {
//        return Mono.fromCallable(() -> stripeClient.accounts()
//                .retrieve(accountId));
//    }
//
//    @Override
//    public Mono<Account> createStripeAccount() {
//        return ReactiveSecurityContextHolder.getContext()
//                .map(SecurityContext::getAuthentication)
//                .flatMap(authentication -> {
//                    return findById(authentication.getName())
//                            .flatMap(user -> {
//                                return Mono.fromCallable(() -> {
//                                    AccountCreateParams accountCreateParams = AccountCreateParams.builder()
//                                            .setEmail(user.getEmail())
//                                            .setType(AccountCreateParams.Type.STANDARD)
//                                            .setCountry("MY")
//                                            .setMetadata(Map.of("user_id", authentication.getName()))
//                                            .build();
//
//                                    Account account = stripeClient.accounts()
//                                            .create(accountCreateParams);
//
//                                    return account;
//                                });
//                            });
//                });
//    }
//
//    @Override
//    public Mono<String> createStripeAccountOnboardingLink(String accountId) {
//        return Mono.fromCallable(() -> {
//            AccountLinkCreateParams accountLinkCreateParams =
//                    AccountLinkCreateParams.builder()
//                            .setAccount(accountId)
//                            .setRefreshUrl("https://example.com/reauth")
//                            .setReturnUrl("http://localhost:8090/user-onboarding/service-provider/completed")
//                            .setType(AccountLinkCreateParams.Type.ACCOUNT_ONBOARDING)
//                            .build();
//
//            AccountLink accountLink = stripeClient.accountLinks()
//                    .create(accountLinkCreateParams);
//            return accountLink.getUrl();
//        });
//    }
////    @Override
////    public Mono<String> createStripeAccount() {
////
////        return ReactiveSecurityContextHolder.getContext()
////                .map(SecurityContext::getAuthentication)
////                .flatMap(authentication -> {
////                    return findById(authentication.getName())
////                            .flatMap(user -> {
////                                return Mono.fromCallable(() -> {
////                                            AccountCreateParams accountCreateParams = AccountCreateParams.builder()
////                                                    .setEmail(user.getEmail())
////                                                    .setType(AccountCreateParams.Type.STANDARD)
////                                                    .setCountry("MY")
////                                                    .setMetadata(Map.of("user_id", authentication.getName()))
////                                                    .build();
////
////                                            Account account = stripeClient.accounts()
////                                                    .create(accountCreateParams);
////
////                                            AccountLinkCreateParams accountLinkCreateParams =
////                                                    AccountLinkCreateParams.builder()
////                                                            .setAccount(account.getId())
////                                                            .setRefreshUrl("https://example.com/reauth")
////                                                            .setReturnUrl("http://localhost:8090/user-onboarding/service-provider/completed")
////                                                            .setType(AccountLinkCreateParams.Type.ACCOUNT_ONBOARDING)
////                                                            .build();
////
////                                            AccountLink accountLink = stripeClient.accountLinks()
////                                                    .create(accountLinkCreateParams);
////                                            return accountLink.getUrl();
////                                        })
////                                        .subscribeOn(Schedulers.boundedElastic());
////                            });
////                });
////
////        // keycloak.realm("LFHardware").users().get("a").setCredentialUserLabel();
////    }
//
//    @Override
//    public Mono<UserRepresentation> findById(String id) {
//        return Mono.fromCallable(() -> keycloak.realm(this.keycloakProperties.getRealm())
//                .users()
//                .get(id)
//                .toRepresentation());
//    }
//
//    @Override
//    public Mono<Void> resetPassword(ResetPasswordInput resetPasswordInput) {
//        return ReactiveSecurityContextHolder.getContext()
//                .map(SecurityContext::getAuthentication)
//                .flatMap(authentication -> {
//                    return Mono.fromCallable(() -> {
//                                try {
//                                    UserResource userResource = keycloak.realm(keycloakProperties.getRealm())
//                                            .users()
//                                            .get(authentication.getName());
//                                    CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
//                                    credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
//                                    credentialRepresentation.setValue(resetPasswordInput.getPassword());
//                                    userResource.resetPassword(credentialRepresentation);
//                                    return null;
//                                } catch (ClientErrorException clientErrorException) {
//                                    throw objectMapper.readValue(getErrorMessage(clientErrorException.getResponse()), InvalidPasswordException.class);
//                                }
//                            })
//                            .subscribeOn(Schedulers.boundedElastic());
//                })
//                .then();
//    }
//
//    public Mono<OTPQrCodeDTO> generateOtpQrCode() {
//        return ReactiveSecurityContextHolder.getContext()
//                .map(securityContext -> (Jwt) securityContext.getAuthentication()
//                        .getCredentials())
//                .flatMap(jwt -> {
//                    return keycloakAccountService.generateOtpQrCode(jwt.getTokenValue());
//                });
//    }
//
//    public Mono<Void> setupOTPVerification(OtpDTO otpDTO) {
//        return ReactiveSecurityContextHolder.getContext()
//                .map(securityContext -> (Jwt) securityContext.getAuthentication()
//                        .getCredentials())
//                .flatMap(jwt -> keycloakAccountService.setupOTPVerification(jwt.getTokenValue(), otpDTO));
//    }
//
//    public Mono<List<UserCredentialsDTO>> findAllUserCredentials() {
//        return ReactiveSecurityContextHolder.getContext()
//                .map(SecurityContext::getAuthentication)
//                .flatMap(authentication ->
//                        Mono.fromCallable(() -> keycloak.realm(keycloakProperties.getRealm())
//                                .users()
//                                .get(authentication.getName())
//                                .credentials()
//                                .stream()
//                                .map(credentialMapper::mapToUserCredentialsDTO)
//                                .collect(Collectors.toList())));
//    }
//
//    @Override
//    public Mono<Void> updateCurrentlyLoggedInUserAccount(UserProfileDTO userProfileDTO) {
////        return Mono.fromCompletionStage(sessionFactory.withTransaction((session, transaction) -> userRepository.findByEmailAddress(session, username)
////                .thenCompose(user -> {
////
////                    AddressDTO addressDTO = userProfileDTO.getProfile()
////                            .getAddress();
////                    ProfileDTO profileDTO = userProfileDTO.getProfile();
////                    Profile profile = new Profile();
////
////                    if (Objects.nonNull(profile)) {
////                        profile.setPhoneNumber(profileDTO.getPhoneNumber());
////                        profile.setEmailAddress(profileDTO.getEmailAddress());
////
////                        if (Objects.nonNull(addressDTO)) {
////                            Address address = new Address();
////                            address.setAddressLine1(address.getAddressLine1());
////                            //profile.setAddress(new Address(addressDTO.getAddressLine1(), addressDTO.getAddressLine2(), addressDTO.getState(), addressDTO.getCity(), addressDTO.getZipcode()));
////                        }
////                    }
////                    user.setProfile(profile);
////
////                    return acc.save(session, user);
////                })));
//        return Mono.empty();
//    }
//
//    public Mono<Void> updateAccountRoleById(String id, List<RoleRepresentation> roleRepresentations) {
//        return ReactiveSecurityContextHolder.getContext()
//                .map(SecurityContext::getAuthentication)
//                .flatMap(authentication -> {
//                    return Mono.fromRunnable(() -> {
//                        keycloak.realm(this.keycloakProperties.getRealm())
//                                .users()
//                                .get(id)
//                                .roles()
//                                .realmLevel()
//                                .add(roleRepresentations);
//                    });
//                });
//    }
//
//
//    private String getErrorMessage(Response response) {
//        Object entity = response.getEntity();
//        String errorMessage = "(none)";
//        if (entity instanceof ErrorRepresentation)
//            errorMessage = ((ErrorRepresentation) entity).getErrorMessage();
//        else if (entity instanceof InputStream)
//            errorMessage = new BufferedReader(new InputStreamReader((InputStream) entity)).lines()
//                    .collect(Collectors.joining("\n"));
//        else if (entity != null)
//            errorMessage = entity.toString();
//        return errorMessage;
//    }
}
