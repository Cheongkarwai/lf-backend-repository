package com.lfhardware.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lfhardware.address.mapper.AddressMapper;
import com.lfhardware.auth.domain.Address;
import com.lfhardware.auth.domain.Profile;
import com.lfhardware.auth.dto.*;
import com.lfhardware.auth.mapper.RoleMapper;
import com.lfhardware.auth.mapper.UserDetailsMapper;
import com.lfhardware.auth.mapper.UserRecordMapper;
import com.lfhardware.auth.mapper.UserRoleMapper;
import com.lfhardware.customer.repository.ICustomerRepository;
import com.lfhardware.auth.repository.IRoleRepository;
import com.lfhardware.auth.repository.IUserRepository;
import com.lfhardware.auth.repository.IUserRoleRepository;
import com.lfhardware.cart.domain.Cart;
import com.lfhardware.cart.repository.CartRepository;
import com.lfhardware.configuration.KeycloakProperties;
import com.lfhardware.email.service.IEmailService;
import com.lfhardware.provider.service.IProviderService;
import com.lfhardware.shared.Pageable;
import com.stripe.StripeClient;
import com.stripe.exception.StripeException;
import com.stripe.param.CustomerCreateParams;
import io.vertx.ext.mail.MailResult;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.reactive.stage.Stage;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class UserDetailsService implements IUserService {

    private final ObjectMapper objectMapper;
    private final IUserRepository userRepository;

    private final IUserRoleRepository userRoleRepository;
    private final IRoleRepository roleRepository;

    private final IEmailService emailService;

    private final IProviderService providerService;

    private final Stage.SessionFactory sessionFactory;

    private final UserRecordMapper userRecordMapper;

    private final RoleMapper roleMapper;

    private final UserDetailsMapper userDetailsMapper;

    private final StripeClient stripeClient;

    private CartRepository cartRepository;

    private final Keycloak keycloak;

    private final KeycloakProperties keycloakProperties;

    private final ICustomerRepository customerRepository;

    private final AddressMapper addressMapper;

    public UserDetailsService(ObjectMapper objectMapper,
                              IUserRepository userRepository, IRoleRepository roleRepository,
                              IUserRoleRepository userRoleRepository, @Qualifier("vertxMailService") IEmailService emailService,
                              IProviderService providerService, Stage.SessionFactory sessionFactory,
                              UserRecordMapper userRecordMapper, RoleMapper roleMapper, UserDetailsMapper userDetailsMapper,
                              StripeClient stripeClient, CartRepository cartRepository,
                              Keycloak keycloak, ICustomerRepository customerRepository,
                              AddressMapper addressMapper, KeycloakProperties keycloakProperties) {
        this.objectMapper = objectMapper;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
        this.emailService = emailService;
        this.providerService = providerService;
        this.sessionFactory = sessionFactory;
        this.userRecordMapper = userRecordMapper;
        this.roleMapper = roleMapper;
        this.userDetailsMapper = userDetailsMapper;
        this.stripeClient = stripeClient;
        this.cartRepository = cartRepository;
        this.keycloak = keycloak;
        this.customerRepository = customerRepository;
        this.addressMapper = addressMapper;
        this.keycloakProperties = keycloakProperties;
    }

//    @Override
//    public Mono<UserDetails> findByUsername(String username) {
//        return Mono.fromFuture(sessionFactory.withSession(session -> userRepository.findUserRoleById(session, username)
//                .thenApply(user -> {
//                    if (Objects.isNull(user)) {
//                        return null;
//                    }
//                    return Stream.of(user).map(UserDetailsMapper::map).findFirst().get();
//                })).toCompletableFuture());
//    }


    public Function<UserAccountDTO, com.lfhardware.auth.domain.User> userMapper() {

        return userAccountDTO -> com.lfhardware.auth.domain.User.builder()
                .username(userAccountDTO.getUsername())
//                .password(passwordEncoder.encode(userAccountDTO.getPassword()))
                .profile(Profile.builder()
                        .emailAddress(userAccountDTO.getProfile()
                                .getEmailAddress())
                        .phoneNumber(userAccountDTO.getProfile()
                                .getPhoneNumber())
//                        .address(Address.builder()
//                                .addressLine1(userAccountDTO.getProfileDTO().getAddressDTO().getAddressLine1())
//                                .addressLine2(userAccountDTO.getProfileDTO().getAddressDTO().getAddressLine2())
//                                .state(userAccountDTO.getProfileDTO().getAddressDTO().getState())
//                                .city(userAccountDTO.getProfileDTO().getAddressDTO().getCity())
//                                .zipcode(userAccountDTO.getProfileDTO().getAddressDTO().getZipcode())
//                                .build())
                        .build())
                .build();
    }

    @Override
    public Mono<Void> save(UserDTO userDTO) {
        return null;
    }

    @Override
    public Mono<UserDTO> save(UserAccountDTO userAccount) {

        return Mono.empty();

//        UserRecord.CreateRequest request = new UserRecord.CreateRequest();
//        request.setDisabled(userAccount.isDisabled());
//        request.setPassword(userAccount.getPassword());
//        request.setEmail(userAccount.getUsername());
//        request.setEmailVerified(false);
//        request.setDisplayName(userAccount.getUsername());
//        request.setPhoneNumber(userAccount.getProfile()
//                .getPhoneNumber());
//
//        return Mono.defer(() -> ApiFutureUtil.toMono(FirebaseAuth.getInstance()
//                        .createUserAsync(request)))
//                .flatMap(userRecord -> Mono.defer(() -> {
//
//                    Set<String> rolesName = userAccount.getRoles()
//                            .stream()
//                            .map(RoleDTO::getName)
//                            .collect(Collectors.toSet());
//
//                    Mono<Void> setCustomUserClaimsMono = ApiFutureUtil.toMono(FirebaseAuth.getInstance()
//                            .setCustomUserClaimsAsync(userRecord.getUid(), Map.of("roles", rolesName, "address", userAccount.getProfile()
//                                    .getAddress())));
//
//                    User user = User.builder()
//                            .username(userAccount.getUsername())
//                            .profile(Profile.builder()
//                                    .emailAddress(userAccount.getProfile()
//                                            .getEmailAddress())
//                                    .phoneNumber(userAccount.getProfile()
//                                            .getPhoneNumber())
////                                    .address(Address.builder().addressLine1(userAccount.getProfile().getAddress().getAddressLine1())
////                                            .addressLine2(userAccount.getProfile().getAddress().getAddressLine2())
////                                            .state(userAccount.getProfile().getAddress().getState())
////                                            .city(userAccount.getProfile().getAddress().getCity())
////                                            .zipcode(userAccount.getProfile().getAddress().getZipcode())
////                                            .build())
//                                    .build())
//                            .build();
//
//                    Mono<UserDTO> saveUserMono = Mono.fromCompletionStage(sessionFactory.withTransaction((session, transaction) -> roleRepository.findByNameIn(session, rolesName.stream()
//                                    .toList())
//                            .thenCompose(roles -> {
//                                Set<UserRole> userRoles = roles.stream()
//                                        .map(role -> {
//                                            UserRole userRole = new UserRole();
//                                            userRole.setUser(user);
//                                            userRole.setRole(role);
//                                            return userRole;
//                                        })
//                                        .collect(Collectors.toSet());
//                                user.setUserRoles(userRoles);
//                                return userRepository.save(session, user);
//                            })
//                            .thenApply(e -> userRecordMapper.mapToUserDTO(userRecord))));
//
//
//                    return Mono.zip(setCustomUserClaimsMono, saveUserMono, createCustomerCart(userAccount));
//
//
//                }))
//                .map(Tuple2::getT2);
    }

    @Override
    public Mono<Void> update(String username, UserProfileDTO userProfileDTO) {
        return Mono.fromCompletionStage(sessionFactory.withTransaction((session, transaction) -> userRepository.findByEmailAddress(session, username)
                .thenCompose(user -> {

                    AddressDTO addressDTO = userProfileDTO.getProfile()
                            .getAddress();
                    ProfileDTO profileDTO = userProfileDTO.getProfile();
                    Profile profile = new Profile();

                    if (Objects.nonNull(profile)) {
                        profile.setPhoneNumber(profileDTO.getPhoneNumber());
                        profile.setEmailAddress(profileDTO.getEmailAddress());

                        if (Objects.nonNull(addressDTO)) {
                            Address address = new Address();
                            address.setAddressLine1(address.getAddressLine1());
                            //profile.setAddress(new Address(addressDTO.getAddressLine1(), addressDTO.getAddressLine2(), addressDTO.getState(), addressDTO.getCity(), addressDTO.getZipcode()));
                        }
                    }
                    user.setProfile(profile);

                    return userRepository.save(session, user);
                })));
    }

    @Override
    public Mono<UserDTO> linkSocialAccount(UserAccountDTO userAccountDTO) {

//        Set<String> rolesName = userAccountDTO.getRoles().stream()
//                .map(RoleDTO::getName).collect(Collectors.toSet());
//
//        Mono<Void> setCustomUserClaimsMono = ApiFutureUtil.toMono(FirebaseAuth.getInstance()
//                .setCustomUserClaimsAsync(userAccountDTO.getUid(), Map.of("roles", rolesName)));
//
//        //Create user entity
//        User user = User.builder()
//                .username(userAccountDTO.getUsername()).profile(Profile.builder().emailAddress(userAccountDTO.getProfile().getEmailAddress())
//                        .phoneNumber(userAccountDTO.getProfile().getPhoneNumber())
//                        .address(Address.builder().addressLine1(userAccountDTO.getProfile().getAddress().getAddressLine1())
//                                .addressLine2(userAccountDTO.getProfile().getAddress().getAddressLine2())
//                                .state(userAccountDTO.getProfile().getAddress().getState())
//                                .city(userAccountDTO.getProfile().getAddress().getCity())
//                                .zipcode(userAccountDTO.getProfile().getAddress().getZipcode())
//                                .build()).build()).build();
//
//        Mono<UserDTO> saveUserMono = Mono.fromCompletionStage(sessionFactory.withTransaction((session, transaction) -> roleRepository.findByNameIn(session, rolesName.stream().toList())
//                .thenCompose(roles -> {
//                    Set<UserRole> userRoles = roles.stream().map(role -> {
//                        UserRole userRole = new UserRole();
//                        userRole.setUser(user);
//                        userRole.setRole(role);
//                        return userRole;
//                    }).collect(Collectors.toSet());
//                    user.setUserRoles(userRoles);
//                    return userRepository.save(session, user);
//                })
//                .thenApply(e -> userRecordMapper.mapToUserDTO(user))));
//
//        return Mono.zip(saveUserMono, createCustomerCart(userAccountDTO))
//                .map(Tuple2::getT1);
        return Mono.empty();
    }

    private Mono<Void> createCustomerCart(UserAccountDTO userAccountDTO) {
        Cart cart = new Cart();
        cart.setUsername(userAccountDTO.getUsername());
        return Mono.fromCompletionStage(sessionFactory.withTransaction((session, transaction) -> cartRepository.save(session, cart)));
    }

    private Mono<Void> createStripeCustomer(UserAccountDTO userAccountDTO) {

        CustomerCreateParams params =
                CustomerCreateParams.builder()
                        .putMetadata("id", userAccountDTO.getUsername())
                        .setEmail(userAccountDTO.getProfile()
                                .getEmailAddress())
                        .setPhone(userAccountDTO.getProfile()
                                .getPhoneNumber())
                        .build();
        try {
            return Mono.justOrEmpty(stripeClient.customers()
                            .create(params))
                    .flatMap(customer ->
                            Mono.defer(() -> Mono.fromCompletionStage(sessionFactory.withTransaction((session, transaction) -> userRepository.findById(session, userAccountDTO.getUsername())
                                    .thenCompose(user -> {
                                        user.setStripeId(customer.getId());
                                        return userRepository.save(session, user);
                                    })))));
        } catch (StripeException e) {
            return Mono.error(e);
        }
    }

    public Mono<UserRepresentation> findById(String id) {
        return Mono.fromCallable(() -> keycloak.realm(this.keycloakProperties.getRealm())
                .users()
                .get(id)
                .toRepresentation());
    }

    public Mono<Void> updateUserRoleById(String id, List<RoleRepresentation> roleRepresentations) {
        return Mono.fromRunnable(() -> {
            keycloak.realm(this.keycloakProperties.getRealm())
                    .users()
                    .get(id)
                    .roles()
                    .realmLevel()
                    .add(roleRepresentations);
        });
    }

    public Mono<Void> update(String id, UserRepresentation userRepresentation) {
        return Mono.fromRunnable(() -> {
            keycloak.realm(this.keycloakProperties.getRealm())
                    .users()
                    .get(id)
                    .update(userRepresentation);
        });
    }

    @Override
    public Mono<UserDTO> findCurrentlyLoggedInUser() {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .flatMap(authentication -> {
                    Jwt token = (Jwt) authentication.getPrincipal();
                    return Mono.fromCallable(() -> {
                                UserRepresentation userRepresentation = keycloak.realm(this.keycloakProperties.getRealm())
                                        .users()
                                        .get(token.getSubject())
                                        .toRepresentation(true);
                                UserDTO userDTO = userRecordMapper.mapToUserDTO(userRepresentation);
                                Map<String, List<String>> userAttributes = userRepresentation.getAttributes();

                                List<String> phoneNumber = userAttributes.get("phone_number");
                                List<String> addresses = userAttributes.get("address");
                                List<AddressDTO> addressDTOs = new ArrayList<>();
                                if (!addresses.isEmpty()) {
                                    addressDTOs = addresses.stream()
                                            .map(address -> {
                                                try {
                                                    return objectMapper.readValue(address, AddressDTO.class);
                                                } catch (JsonProcessingException e) {
                                                    throw new RuntimeException(e);
                                                }
                                            })
                                            .toList();
                                }
                                userDTO.setRoles(authentication.getAuthorities()
                                        .stream()
                                        .map(roleMapper::mapToRoleDTO)
                                        .collect(Collectors.toSet()));
                                userDTO.setEmailVerified(userRepresentation.isEmailVerified());
                                userDTO.setProfile(ProfileDTO.builder()
                                        .phoneNumber(!phoneNumber.isEmpty() ? phoneNumber.get(0) : null)
                                        .emailAddress(userRepresentation.getEmail())
                                        .address(addressDTOs.get(0))
                                        .build());
                                System.out.println(userRepresentation.getUsername());
                                System.out.println(authentication.getAuthorities());
                                return userDTO;
                            })
                            .flatMap(userDTO -> {
//                                boolean isCustomer = userDTO.getRoles()
//                                        .stream()
//                                        .anyMatch(role -> role.getName()
//                                                .equals(Role.customer.toString()));
//                                if (isCustomer) {
//                                    return Mono.fromCompletionStage(sessionFactory.withSession(session -> customerRepository.findById(session, userDTO.getUid())
//                                            .thenApply(customer -> {
//                                                ProfileDTO profile = userDTO.getProfile();
//                                                profile.setPhoneNumber(customer.getPhoneNumber());
//                                                AddressDTO addressDTO = addressMapper.mapToAddressDTO(customer.getAddress());
//                                                profile.setAddress(addressDTO);
//                                                return userDTO;
//                                            })));
//                                }
                                return Mono.just(userDTO);
                            });
//                    return Mono.empty();
                });
    }

    @Override
    public Mono<UserAccountDTO> findUserAccountByUsername(String username) {
        return Mono.fromCompletionStage(sessionFactory.withSession(session -> userRepository.findById(session, username)
                .thenApply(userRecordMapper::mapToUserAccountDTO)));
    }


    public Mono<UserDetails> findByUsername(String username) {
//        return Mono.defer(() -> ApiFutureUtil.toMono(FirebaseAuth.getInstance()
//                        .getUserAsync(username)))
//                .map(userRecord -> org.springframework.security.core.userdetails.User.withUsername(userRecord.getEmail())
//                        .build());
        return Mono.empty();
    }

    public Mono<UserDTO> findByPhoneNumber(String phoneNumber) {
        return Mono.empty();
//        return Mono.defer(() -> ApiFutureUtil.toMono(FirebaseAuth.getInstance()
//                        .getUserByPhoneNumberAsync(phoneNumber)))
//                .map(userRecordMapper::mapToUserDTO);
    }

    @Override
    public Mono<UserRoleDTO> findUserRoleById(String username) {
        return Mono.fromFuture(sessionFactory.withSession(session -> userRepository.findUserRoleById(session, username)
                        .thenApply(user -> Stream.of(user)
                                .map(UserRoleMapper::map)
                                .findFirst()
                                .get()))
                .toCompletableFuture());
    }

    @Override
    public Mono<MailResult> otpLogin(OtpLoginDTO otpLoginDTO) {
        return emailService.sendOneTimeLoginPasscode(otpLoginDTO.getReceiver());
    }

    @Override
    public Mono<MailResult> forgotPassword(PasswordRecoveryDTO passwordRecoveryDTO) {
        return Mono.empty();
//            String resetPasswordLink = FirebaseAuth.getInstance().generatePasswordResetLink(passwordRecoveryDTO.getEmailAddress());
//            System.out.println(resetPasswordLink);
//        return Mono.defer(() -> ApiFutureUtil.toMono(FirebaseAuth.getInstance()
//                        .generatePasswordResetLinkAsync(passwordRecoveryDTO.getEmailAddress())))
//                .flatMap(link -> {
//                    Context context = new Context();
//                    context.setVariables(Map.of("link", "test", "recipientEmailAddress", passwordRecoveryDTO.getEmailAddress()));
//                    MailInformation mailInformation = new MailInformation();
//                    mailInformation.setContext(context);
//                    mailInformation.setSubject("Password Recovery");
//                    mailInformation.setRecipient(new Recipient(passwordRecoveryDTO.getEmailAddress()));
//                    return emailService.sendForgotPasswordEmail(mailInformation);
//                });

//                })
//                        .flatMap(link->{
//                            System.out.println(link);
//                            Context context = new Context();
//                            context.setVariables(Map.of("link", link, "recipientEmailAddress", passwordRecoveryDTO.getEmailAddress()));
//                            MailInformation mailInformation = new MailInformation();
//                            mailInformation.setContext(context);
//                            mailInformation.setSubject("Password Recovery");
//                            mailInformation.setRecipient(new Recipient(passwordRecoveryDTO.getEmailAddress()));
//                            return emailService.sendForgotPasswordEmail(mailInformation);
//                        });
    }

    @Override
    public Mono<MailResult> verifyEmail(String email) {
        return Mono.empty();
//        return Mono.defer(() -> ApiFutureUtil.toMono(FirebaseAuth.getInstance()
//                        .getUserByEmailAsync(email)))
//                .flatMap(userRecord -> {
//
//                    if (userRecord.isEmailVerified()) {
//                        return Mono.empty();
//                    }
//
//                    return Mono.defer(() -> ApiFutureUtil.toMono(FirebaseAuth.getInstance()
//                                    .generateEmailVerificationLinkAsync(email))
//                            .flatMap(link -> {
//                                MailInformation mailInformation = new MailInformation();
//                                mailInformation.setSubject("Email Verification");
//                                mailInformation.setRecipient(new Recipient(email));
//                                Context context = new Context();
//                                context.setVariables(Map.of("link", link, "emailAddress", email));
//                                mailInformation.setContext(context);
//                                return emailService.sendEmailVerification(mailInformation, link);
//                            }));
//                });
    }

    @Override
    public Mono<Void> saveServiceProviderAccount(ServiceProviderAccountDTO serviceProviderAccountDTO) {

        Mono<UserDTO> saveServiceProviderAccountMono = this.save(serviceProviderAccountDTO.getAccount());
        //Mono<Void> saveServiceProviderMono = providerService.save(serviceProviderAccountDTO.getServiceProvider());

        //return saveServiceProviderMono.then(Mono.defer(() -> saveServiceProviderAccountMono)).then();
        return Mono.empty();
    }

    @Override
    public Mono<com.lfhardware.auth.domain.User> changePassword(ChangePasswordDTO changePasswordDTO) {

        if (!changePasswordDTO.getPassword()
                .equals(changePasswordDTO.getConfirmationPassword())) {
            return Mono.error(new RuntimeException("No"));
        }


        return Mono.fromFuture(sessionFactory.withTransaction((session, transaction) -> userRepository.findByEmailAddress(session, changePasswordDTO.getEmailAddress()))
                        .toCompletableFuture())
                .flatMap(user -> {
//                    if (user.getPassword().equals(changePasswordDTO.getPassword())) {
//                        return Mono.error(new RuntimeException("Haaa"));
//                    }
                    return Mono.fromFuture(sessionFactory.withTransaction(((session, transaction) -> {
                                // user.setPassword(changePasswordDTO.getPassword());
                                return userRepository.merge(session, user);
                            }))
                            .toCompletableFuture());

                });

    }

    public Mono<UserDTO> findByEmailAddress(String emailAddress) {
        return Mono.fromCompletionStage(sessionFactory.withSession(session -> {
            return userRepository.findById(session, emailAddress)
                    .thenApply(userRecordMapper::mapToUserDTO);
        }));
    }

    @Override
    public Mono<Pageable<UserDTO>> findAll(UserPageRequest userPageRequest) {

        return Mono.fromCallable(() -> {

            UsersResource userResource = keycloak.realm(keycloakProperties.getRealm())
                    .users();

            List<UserRepresentation> userRepresentations = userResource
                    .list(userPageRequest.getPage() * userPageRequest.getPageSize(), userPageRequest.getPageSize());

            return new Pageable<>(userRepresentations.stream()
                    .map(userRecordMapper::mapToUserDTO)
                    .collect(Collectors.toList()),
                    userPageRequest.getPageSize(),
                    userPageRequest.getPage(),
                    userResource.count());
        });

//        return ReactiveSecurityContextHolder.getContext()
//                .map(securityContext -> {
//                    System.out.println(securityContext.getAuthentication());
//                    return securityContext.getAuthentication();
//                })
//                .flatMap(authentication -> {
//                    return Mono.fromCompletionStage(sessionFactory.withSession(session -> userRepository.findAll(session, userPageRequest)
//                                            .thenApply(users -> users.stream()
//                                                    .map(userRecordMapper::mapToUserDTO)
//                                                    .collect(Collectors.toList())))
//                                    .thenCombine(sessionFactory.withSession(session -> userRepository.count(session, userPageRequest)), ((userDTOS, totalElements) -> {
//                                        return new Pageable<>(userDTOS, userPageRequest.getPageSize(), userPageRequest.getPage(), totalElements.intValue());
//                                    })))
//                            .doOnSuccess((e) -> System.out.println("Success"))
//                            .doOnError(e -> System.out.println(e.getMessage()));
//                });
    }

    @Override
    public Mono<List<RoleDTO>> findAllRoles() {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .flatMap(authentication -> findAllRolesById(authentication.getName()));
    }

    @Override
    public Mono<List<RoleDTO>> findAllRolesById(String id) {
        return Mono.fromCallable(() -> keycloak.realm(keycloakProperties.getRealm())
                .users()
                .get(id)
                .roles()
                .realmLevel()
                .listAll()
                .stream()
                .map(roleMapper::mapToRoleDTO)
                .collect(Collectors.toList()));
    }

    @Override
    public Mono<Long> count() {
        return Mono.fromCompletionStage(sessionFactory.withSession(session -> userRepository.count(session, new UserPageRequest())));
    }

    @Override
    public Mono<List<DailyUserStat>> findDailyUserCount(Integer days) {
        return Mono.fromCompletionStage(sessionFactory.withSession(session -> userRepository.countDailyUserGroupByDays(session, days)));
//                .flatMap(dailyUserStats -> {
//                    LocalDateTime today = LocalDateTime.now();
//                    LocalDateTime daysBefore = today.minusDays(days);
//                    List<DailyUserStat> dailyUserStatList = new ArrayList<>();
////
////
//
//                    for(LocalDateTime date = daysBefore; date.isBefore(today); date = date.plusDays(1)){
//                        for(DailyUserStat dailyUserStat: dailyUserStats){
//                            System.out.println("Looping");
//                            System.out.println(dailyUserStat.getDay());
//                            System.out.println(date.getDayOfMonth());
//                            if(dailyUserStat.getDay().equals(String.valueOf(date.getDayOfMonth()))){
//                                dailyUserStatList.add(new DailyUserStat(date.toLocalDate().toString(),dailyUserStat.getTotal()));
//                                break;
//                            }
//                        }
//                        dailyUserStatList.add(new DailyUserStat(date.toLocalDate().toString(),0));
//                    }
//
//                    return Mono.just(dailyUserStats);
//
//                });
//                .flatMap(dailyUserStats->Mono.fromCallable(()->{
//                    System.out.println("Executing");
//                    LocalDateTime today = LocalDateTime.now();
//                    LocalDateTime daysBefore = today.minusDays(days);
//                    List<DailyUserStat> dailyUserStatList = new ArrayList<>();
//                    first:
//                    for (LocalDateTime date = daysBefore; daysBefore.isBefore(today); date = date.plusDays(1)) {
//
//                        second:
//                        for(DailyUserStat dailyUserStat : dailyUserStatList){
//                            if(dailyUserStat.getDay().equals(date.getDayOfMonth())){
//                                dailyUserStats.add(new DailyUserStat(date.toLocalDate().toString(), Integer.parseInt(String.valueOf(dailyUserStat.getTotal()))));
//
//                                continue first;
//                            }
//                        }
//                        dailyUserStatList.add(new DailyUserStat(date.toLocalDate().toString(), 0));
//                    }
//
//                    return dailyUserStatList;
//                }).subscribeOn(Schedulers.boundedElastic()));
    }
}
