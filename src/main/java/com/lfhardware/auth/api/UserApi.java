package com.lfhardware.auth.api;

import com.lfhardware.auth.dto.*;
import com.lfhardware.auth.service.IUserService;
import com.lfhardware.auth.dto.ServiceProviderAccountDTO;
import com.lfhardware.shared.ErrorResponse;
import com.lfhardware.shared.Sort;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.*;

@Component
@Slf4j
public class UserApi {

    private final IUserService userService;


    private final Validator validator;

    public UserApi(IUserService userService, Validator validator) {
        this.userService = userService;
        this.validator = validator;
    }

    public Mono<ServerResponse> findAll(ServerRequest serverRequest) {

        UserPageRequest userPageRequest = new UserPageRequest();

        userPageRequest.setPage(Integer.parseInt(serverRequest.queryParam("page")
                .orElseThrow(() -> new NoSuchElementException("Page query parameter not found"))));
        userPageRequest.setPageSize(Integer.parseInt(serverRequest.queryParam("page_size")
                .orElseThrow(() -> new NoSuchElementException("Size query parameter not found"))));
        userPageRequest.setSort(new Sort(serverRequest.queryParam("sort")
                .orElse("")));

        return userService.findAll(userPageRequest)
                .flatMap(user -> ServerResponse.ok()
                        .bodyValue(user));
    }

    public Mono<ServerResponse> findUser(ServerRequest serverRequest) {
        return userService.findByEmailAddress(serverRequest.pathVariable("username"))
                .flatMap(userDTO -> ServerResponse.ok()
                        .bodyValue(userDTO))
                .switchIfEmpty(ServerResponse.notFound()
                        .build())
                .onErrorResume(throwable -> {
                    return ServerResponse.badRequest()
                            .build();
                });
    }

    public Mono<ServerResponse> findCurrentlyLoggedInUser(ServerRequest serverRequest) {
        return userService.findCurrentlyLoggedInUser()
                .flatMap(userAccount -> ServerResponse.ok()
                        .bodyValue(userAccount));
    }

    public Mono<ServerResponse> findUserByPhoneNumber(ServerRequest serverRequest) {
        return userService.findByPhoneNumber(serverRequest.pathVariable("phoneNumber"))
                .flatMap(userDTO -> ServerResponse.ok()
                        .bodyValue(userDTO))
                .onErrorResume(throwable -> ServerResponse.notFound()
                        .build());
    }

    public Mono<ServerResponse> findUserRole(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .body(userService.findUserRoleById(serverRequest.pathVariable("username")), UserRoleDTO.class);
    }

    public Mono<ServerResponse> sendAccountRecoveryEmail(ServerRequest serverRequest) {
        Mono<PasswordRecoveryDTO> passwordRecoveryDTOMono = serverRequest.bodyToMono(PasswordRecoveryDTO.class)
                .doOnNext(passwordRecoveryDTO -> {
                    Set<ConstraintViolation<PasswordRecoveryDTO>> constraintViolations = validator.validate(passwordRecoveryDTO);
                    if (!constraintViolations.isEmpty()) {
                        log.debug("Validation errors: {}", constraintViolations);
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
                    }
                });
        return passwordRecoveryDTOMono
                .map(userService::forgotPassword)
                .then(Mono.defer(() -> ServerResponse.ok()
                        .build()));
    }


    public Mono<ServerResponse> register(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(UserAccountDTO.class)
                .flatMap(userAccountDTO -> {
                    Set<ConstraintViolation<UserAccountDTO>> constraintViolations = validator.validate(userAccountDTO);
                    if (!constraintViolations.isEmpty()) {
                        return Mono.error(new ConstraintViolationException("Validation failed", constraintViolations));
                    }
                    return Mono.just(userAccountDTO);
                })
                .flatMap(userAccountDTO -> {
                    if (serverRequest.queryParam("social_login")
                            .isPresent()) {
                        return userService.linkSocialAccount(userAccountDTO);
                    }
                    return userService.save(userAccountDTO);
                })
                .flatMap(user -> ServerResponse.ok()
                        .bodyValue(user))
                .onErrorResume(throwable -> ServerResponse.badRequest()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(new ErrorResponse(throwable.getMessage(), serverRequest.path())))
                .log();

    }

    public Mono<ServerResponse> registerServiceProvider(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(ServiceProviderAccountDTO.class)
                .flatMap(userService::saveServiceProviderAccount)
                .then(Mono.defer(() -> ServerResponse.ok()
                        .build()))
                .onErrorResume(throwable -> ServerResponse.badRequest()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(new ErrorResponse(throwable.getMessage(), serverRequest.path())))
                .log();
    }

    public Mono<ServerResponse> verifyEmail(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(new ParameterizedTypeReference<Map<String, String>>() {
                })
                .flatMap(map -> userService.verifyEmail(map.get("email"))
                        .flatMap(e -> ServerResponse.ok()
                                .build())
                        .switchIfEmpty(ServerResponse.noContent()
                                .build())
                        .onErrorResume(throwable -> ServerResponse.badRequest()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(new ErrorResponse(throwable.getMessage(), serverRequest.path()))));
    }

    public Mono<ServerResponse> findCurrentlyLoggedInUserRoles(ServerRequest serverRequest) {
        return userService.findAllRoles()
                .flatMap(rolesDTO -> ServerResponse.ok()
                        .bodyValue(rolesDTO));
    }

    public Mono<ServerResponse> findUserAccountByUsername(ServerRequest serverRequest) {
        return userService.findByEmailAddress(serverRequest.pathVariable("username"))
                .flatMap(userDTO -> ServerResponse.ok()
                        .bodyValue(userDTO))
                .switchIfEmpty(ServerResponse.notFound()
                        .build())
                .onErrorResume(throwable -> ServerResponse.badRequest()
                        .build());
    }

    public Mono<ServerResponse> count(ServerRequest serverRequest) {

        return ServerResponse.ok()
                .body(userService.count(), Long.class);
    }

    public Mono<ServerResponse> findDailyUserCount(ServerRequest serverRequest) {

        Integer days = Integer.valueOf(serverRequest.queryParam("days")
                .orElse(null));
        return userService.findDailyUserCount(days)
                .flatMap(dailyUser -> ServerResponse.ok()
                        .bodyValue(dailyUser));
    }

    public Mono<ServerResponse> updateUser(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(UserProfileDTO.class)
                .flatMap(userProfile -> userService.update(serverRequest.pathVariable("username"), userProfile))
                .flatMap(e -> ServerResponse.ok()
                        .build())
                .onErrorResume(throwable -> ServerResponse.badRequest()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(new ErrorResponse(throwable.getMessage(), serverRequest.path())));
    }

    public Mono<ServerResponse> findLoggedInUsername(ServerRequest serverRequest) {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .map(authentication -> {
                    System.out.println(authentication.getName());
                    return authentication.getName();
                })
                .flatMap(name -> ServerResponse.ok()
                        .bodyValue(name));
    }
}
