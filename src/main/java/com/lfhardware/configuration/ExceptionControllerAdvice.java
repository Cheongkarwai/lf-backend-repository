package com.lfhardware.configuration;

import com.lfhardware.core.dto.ErrorResponse;
import com.lfhardware.product.exception.ProductNotFoundException;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(exception = {ProductNotFoundException.class})
    public Mono<HttpEntity<ErrorResponse>> handleNotFoundException(RuntimeException ex, ServerHttpRequest request){
        return Mono.just(ResponseEntity.status(404).body(ErrorResponse.builder().timestamp(OffsetDateTime.now())
                .path(request.getPath().value()).message(ex.getMessage()).build()));
    }

    @ExceptionHandler(exception = WebExchangeBindException.class)
    public Mono<HttpEntity<Object>> handleNotFoundException(WebExchangeBindException exception, ServerHttpRequest request){
        return Mono.just(ResponseEntity.ok(exception.getAllErrors()));
    }
}
