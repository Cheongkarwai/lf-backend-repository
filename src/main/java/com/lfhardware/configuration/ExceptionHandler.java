package com.lfhardware.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lfhardware.core.exception.CustomException;
import com.lfhardware.core.exception.RateLimitException;
import com.lfhardware.core.exception.ServiceNotFoundException;
import com.lfhardware.shared.ErrorResponse;
import io.github.bucket4j.Bucket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Component
@Slf4j
public class ExceptionHandler implements ErrorWebExceptionHandler {

    private final ObjectMapper objectMapper;

    private final Bucket bucket;
    public ExceptionHandler(ObjectMapper objectMapper, Bucket bucket){
        this.objectMapper = objectMapper;
        this.bucket = bucket;
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {

        log.debug("Error message: {}",ex.getMessage());


        ErrorResponse errorResponse = ErrorResponse.builder()
                .path(exchange.getRequest().getPath().toString())
                .timestamp(LocalDateTime.now())
                .message(ex.getMessage())
               // .requestId(exchange.getRequest().getId())
                .build();


        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);

        if(ex instanceof SQLException sqlException){
            exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
        }
        if(ex instanceof RateLimitException){
            exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
        }

        if(ex instanceof ServiceNotFoundException serviceNotFoundException){
            errorResponse.setCode(serviceNotFoundException.getCode());
            exchange.getResponse().setStatusCode(HttpStatus.NOT_FOUND);
        }

        DataBuffer dataBuffer = null;
        try {
            dataBuffer = exchange.getResponse().bufferFactory().wrap(objectMapper.writeValueAsBytes(errorResponse));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

//        if(ex instanceof FirebaseAuthException firebaseAuthException){
//            exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
//        }

        return exchange.getResponse().writeWith(Mono.just(dataBuffer));

    }
}
