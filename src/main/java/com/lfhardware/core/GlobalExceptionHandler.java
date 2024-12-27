package com.lfhardware.core;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<ProblemDetail>> handleMethodArgumentNotValidException(WebExchangeBindException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatusCode.valueOf(400));
        List<String> errorMessages = exception.getBindingResult().getFieldErrors().stream()
                .map(fieldError-> String.format("%s.%s %s",fieldError.getObjectName(),fieldError.getField(),fieldError.getDefaultMessage()))
                .toList();
        problemDetail.setProperties(Map.of("errors", errorMessages));
        return Mono.just(ResponseEntity.badRequest().body(problemDetail));
    }
}
