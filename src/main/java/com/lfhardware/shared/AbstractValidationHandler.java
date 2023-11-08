package com.lfhardware.shared;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.Set;

public abstract class AbstractValidationHandler<T,U extends Validator> {

    private final Class<T> validationClass;

    private final U validator;

    protected AbstractValidationHandler(Class<T> clazz,U validator){
        this.validationClass = clazz;
        this.validator = validator;
    }

//    public final Mono<T> handleRequest(T account){
//
//         Set<ConstraintViolation<T>> constraintViolations = this.validator.validate(account);
//
//         if(!constraintViolations.isEmpty()){
//             return onValidationError(constraintViolations,account);
//         }
//         return Mono.just(account);
//
//    }

    protected Mono<ServerResponse> onValidationError(Set<ConstraintViolation<T>> constraintViolations,T body){
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,constraintViolations.toString());
    }
}
