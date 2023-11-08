package com.lfhardware.auth.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lfhardware.auth.domain.Address;
import com.lfhardware.auth.domain.Profile;
import com.lfhardware.auth.domain.UserRole;
import com.lfhardware.auth.dto.*;
import com.lfhardware.auth.mapper.UserDetailsMapper;
import com.lfhardware.auth.mapper.UserMapper;
import com.lfhardware.auth.mapper.UserRoleMapper;
import com.lfhardware.auth.repository.IRoleRepository;
import com.lfhardware.auth.repository.IUserRepository;
import com.lfhardware.email.service.IEmailService;
import io.vertx.ext.mail.MailResult;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.query.Query;
import org.hibernate.reactive.stage.Stage;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.security.auth.login.CredentialException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserDetailsService implements ReactiveUserDetailsService,IUserService {

    private final ObjectMapper objectMapper;
    private final PasswordEncoder passwordEncoder;
    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;

    private final IEmailService emailService;

    private Stage.SessionFactory sessionFactory;

    @PostConstruct
    public void init(){
        sessionFactory = Persistence.createEntityManagerFactory("postgres").unwrap(Stage.SessionFactory.class);
    }

    public UserDetailsService(ObjectMapper objectMapper,PasswordEncoder passwordEncoder,IUserRepository userRepository
            ,IRoleRepository roleRepository,@Qualifier("vertxMailService") IEmailService emailService){
        this.passwordEncoder = passwordEncoder;
        this.objectMapper = objectMapper;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.emailService = emailService;
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return Mono.fromFuture(sessionFactory.withSession(session -> userRepository.findUserRoleById(session,username)
                .thenApply(user->{
                    if(Objects.isNull(user)){
                        return null;
                    }
                    return Stream.of(user).map(UserDetailsMapper::map).findFirst().get();
                })).toCompletableFuture());
    }


    public Function<UserAccountDTO, com.lfhardware.auth.domain.User> userMapper(){

        return userAccountDTO -> com.lfhardware.auth.domain.User.builder()
                .username(userAccountDTO.getUsername())
                .password(passwordEncoder.encode(userAccountDTO.getPassword()))
                .profile(Profile.builder().emailAddress(userAccountDTO.getProfileDTO().getEmailAddress())
                        .phoneNumber(userAccountDTO.getProfileDTO().getPhoneNumber())
                        .address(Address.builder()
                                .addressLine1(userAccountDTO.getProfileDTO().getAddressDTO().getAddressLine1())
                                .addressLine2(userAccountDTO.getProfileDTO().getAddressDTO().getAddressLine2())
                                .state(userAccountDTO.getProfileDTO().getAddressDTO().getState())
                                .city(userAccountDTO.getProfileDTO().getAddressDTO().getCity())
                                .zipcode(userAccountDTO.getProfileDTO().getAddressDTO().getZipcode())
                                .build())
                        .build())
                .build();
    }

    @Override
    public Mono<Void> save(UserDTO userDTO) {
        return null;
    }

    @Override
    public Mono<Void> save(UserAccountDTO userAccountDTO) {

       com.lfhardware.auth.domain.User user = Stream.of(userAccountDTO)
                                                        .map(account->{
                                                           com.lfhardware.auth.domain.User mappedUser = UserMapper.map(account);
                                                           mappedUser.setPassword(passwordEncoder.encode(account.getPassword()));
                                                           return mappedUser;
                                                        })
                                                        .findFirst().get();

        return Mono.fromFuture(()->sessionFactory.withTransaction((session,transaction) -> roleRepository.findAllByIds(session,userAccountDTO.getRoleDTOs().stream().map(RoleDTO::getId).toList())
                                        .thenCompose(roles-> {

                                            Set<UserRole> userRoles = roles.stream()
                                                    .map(role->UserRole.builder()
                                                                .role(role)
                                                                .user(user).build()
                                                    ).collect(Collectors.toSet());
                                            user.setUserRoles(userRoles);

                                            return userRepository.save(session,user);
                                        }))
                                        .toCompletableFuture());


//        return Mono.fromFuture(()->sessionFactory.withTransaction((session, transaction) -> {
//            userAccountDTO.
//            user.setUserRoles(userRol);
//            return userRepository.save(session,user);
//        }).toCompletableFuture());

//        return Mono.just("Hi");


    }

    public Mono<UserDTO> findById(String username){
        return Mono.fromFuture(sessionFactory.withSession(session -> userRepository.findById(session,username)
                .thenApply(user->objectMapper.convertValue(user,UserDTO.class)))
                .toCompletableFuture());
    }

    @Override
    public Mono<UserRoleDTO> findUserRoleById(String username) {
        return Mono.fromFuture(sessionFactory.withSession(session->userRepository.findUserRoleById(session,username)
                .thenApply(user-> Stream.of(user).map(UserRoleMapper::map).findFirst().get()))
                .toCompletableFuture());
    }
    public Mono<MailResult> forgotPassword(PasswordRecoveryDTO passwordRecoveryDTO){
        return emailService.sendForgotPasswordEmail(passwordRecoveryDTO.getEmailAddress());
    }

    public Mono<com.lfhardware.auth.domain.User> changePassword(ChangePasswordDTO changePasswordDTO){

        if(!changePasswordDTO.getPassword().equals(changePasswordDTO.getConfirmationPassword())){
           return Mono.error(new RuntimeException("No"));
        }


        return Mono.fromFuture(sessionFactory.withTransaction((session,transaction) -> userRepository.findByEmailAddress(session, changePasswordDTO.getEmailAddress()))
                .toCompletableFuture())
                .flatMap(user->{
                    if (user.getPassword().equals(changePasswordDTO.getPassword())) {
                        return Mono.error(new RuntimeException("Haaa"));
                    }
                    return Mono.fromFuture(sessionFactory.withTransaction(((session, transaction) -> {
                        user.setPassword(changePasswordDTO.getPassword());
                        return userRepository.merge(session,user);
                    })).toCompletableFuture());

                });

    }
}
