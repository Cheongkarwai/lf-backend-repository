package com.lfhardware.auth.api;

import com.lfhardware.auth.dto.*;
import com.lfhardware.auth.service.UserDetailsService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.Set;

@Component
public class AuthHandler {

  //  private final ReactiveAuthenticationManager reactiveAuthenticationManager;
    private final UserDetailsService userDetailsService;

    private final Validator validator;

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthHandler.class);

    public AuthHandler(UserDetailsService userDetailsService,
                         Validator validator){
        this.userDetailsService = userDetailsService;
        this.validator = validator;
    }

    public Mono<ServerResponse> login(ServerRequest serverRequest){
//        return serverRequest.bodyToMono(UserDTO.class)
//                .flatMap(user->reactiveAuthenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword()))
//                .flatMap(tokenService::generateAccessToken)
//                .flatMap(e->ServerResponse.ok().body(Mono.just(TokenDTO.builder().accessToken(e).build()), TokenDTO.class)))
//                .onErrorResume(throwable-> ServerResponse.badRequest().contentType(MediaType.APPLICATION_JSON).bodyValue(ErrorCode.LOGIN.getErrorCode()));
        return ServerResponse.ok().build();
    }

    public Mono<ServerResponse> register(ServerRequest serverRequest){
        Mono<UserAccountDTO> userAccountDTOMono = serverRequest.bodyToMono(UserAccountDTO.class).doOnNext(userAccountDTO -> {
            Set<ConstraintViolation<UserAccountDTO>> constraintViolations = validator.validate(userAccountDTO);
            if (!constraintViolations.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
        });
        return userAccountDTOMono
                .flatMap(e-> userDetailsService.save(e))
                .then(ServerResponse.noContent().build())
                .onErrorResume(throwable->ServerResponse.badRequest().build());
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
                .onErrorResume(throwable-> ServerResponse.badRequest().contentType(MediaType.APPLICATION_JSON).bodyValue(throwable.getMessage()))
                .log();
    }

    public Mono<ServerResponse> otpLogin(ServerRequest serverRequest) {
        Mono<OtpLoginDTO> passwordRecoveryDTOMono = serverRequest.bodyToMono(OtpLoginDTO.class).doOnNext(otpLoginDTO->{
            Set<ConstraintViolation<OtpLoginDTO>> constraintViolations = validator.validate(otpLoginDTO);
            if(!constraintViolations.isEmpty()){
                LOGGER.debug("Validation errors: {}",constraintViolations);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
        });
        return passwordRecoveryDTOMono
                .map(userDetailsService::otpLogin)
                .then(Mono.defer(() -> ServerResponse.ok().build()));
    }
}

//@Getter
//@Setter
//class ErrorResponse{
//
//    private String message;
//
//    private LocalDateTime timestamp;
//
//    private
//
//    public ErrorResponse(String message){
//        this.message = message;
//    }
//
//}
