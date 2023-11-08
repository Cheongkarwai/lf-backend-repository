package com.lfhardware.auth.handler;

import com.lfhardware.auth.dto.*;
import com.lfhardware.auth.service.TokenService;
import com.lfhardware.auth.service.UserDetailsService;
import com.lfhardware.shared.ErrorCode;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.Set;

@Component
public class AuthHandler {

    private final ReactiveAuthenticationManager reactiveAuthenticationManager;
    private final UserDetailsService userDetailsService;

    private final TokenService tokenService;

    private final Validator validator;

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthHandler.class);

    public AuthHandler(ReactiveAuthenticationManager reactiveAuthenticationManager,UserDetailsService userDetailsService,TokenService tokenService,
                         Validator validator){
        this.reactiveAuthenticationManager = reactiveAuthenticationManager;
        this.userDetailsService = userDetailsService;
        this.tokenService = tokenService;
        this.validator = validator;
    }

    public Mono<ServerResponse> login(ServerRequest serverRequest){
        return serverRequest.bodyToMono(UserDTO.class)
                .flatMap(user->reactiveAuthenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword()))
                .flatMap(tokenService::generateAccessToken)
                .flatMap(e->ServerResponse.ok().body(Mono.just(TokenDTO.builder().accessToken(e).build()), TokenDTO.class)))
                .onErrorResume(throwable-> ServerResponse.badRequest().contentType(MediaType.APPLICATION_JSON).bodyValue(new Test(throwable.getMessage(), ErrorCode.LOGIN.getErrorCode())));
    }

    public Mono<ServerResponse> register(ServerRequest serverRequest){
        Mono<UserAccountDTO> userAccountDTOMono = serverRequest.bodyToMono(UserAccountDTO.class).doOnNext(userAccountDTO -> {
            Set<ConstraintViolation<UserAccountDTO>> constraintViolations = validator.validate(userAccountDTO);
            if (!constraintViolations.isEmpty()) {
                System.out.println(constraintViolations);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
        });
        return userAccountDTOMono
                .flatMap(userDetailsService::save)
                .then(Mono.defer(() -> ServerResponse.noContent().build()));

    }

    public Mono<ServerResponse> sendAccountRecoveryEmail(ServerRequest serverRequest){
        Mono<PasswordRecoveryDTO> passwordRecoveryDTOMono = serverRequest.bodyToMono(PasswordRecoveryDTO.class).doOnNext(passwordRecoveryDTO->{
            Set<ConstraintViolation<PasswordRecoveryDTO>> constraintViolations = validator.validate(passwordRecoveryDTO);
            if(!constraintViolations.isEmpty()){
                LOGGER.debug("Validation errors: {}",constraintViolations);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
        });
        return passwordRecoveryDTOMono
                .map(userDetailsService::forgotPassword)
                .then(Mono.defer(() -> ServerResponse.ok().build()));
    }

    public Mono<ServerResponse> changePassword(ServerRequest serverRequest){
        Mono<ChangePasswordDTO> passwordRecoveryDTOMono = serverRequest.bodyToMono(ChangePasswordDTO.class)
                .doOnNext(changePasswordDTO -> {
                    Set<ConstraintViolation<ChangePasswordDTO>> constraintViolations = validator.validate(changePasswordDTO);
                    if(!constraintViolations.isEmpty()){
                        LOGGER.debug("Validation errors: {}",constraintViolations);
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
                    }
                });

        return passwordRecoveryDTOMono
                .flatMap(userDetailsService::changePassword)
                .flatMap(e->ServerResponse.ok().build())
                .onErrorResume(throwable-> ServerResponse.badRequest().contentType(MediaType.APPLICATION_JSON).bodyValue(new Test(throwable.getMessage())))
                .log();
    }
}

class Test{

    private String name;

    private String code;

    public Test(){}

    public Test(String name){
        this.name = name;
    }

    public Test(String name,String code){
        this.name = name;
        this.code = code;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setCode(String code){
        this.code = code;
    }

    public String getCode(){
        return code;
    }

}
