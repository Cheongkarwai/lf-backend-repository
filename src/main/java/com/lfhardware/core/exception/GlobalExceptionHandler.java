package com.lfhardware.core.exception;

import com.lfhardware.keycloak.KeycloakClientException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(KeycloakClientException.class)
    public Mono<ProblemDetail> handleKeycloakClientException(KeycloakClientException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(400), e.getErrorResponse().getErrorDescription());
        return Mono.just(problemDetail);
    }
}
