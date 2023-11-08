package com.lfhardware.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lfhardware.shared.ErrorResponse;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Component
public class ExceptionHandler implements ErrorWebExceptionHandler {

    private final ObjectMapper objectMapper;
    public ExceptionHandler(ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {

        ex.printStackTrace();

        System.out.println("Hi");

        ErrorResponse errorResponse = ErrorResponse.builder()
                .path(exchange.getRequest().getPath().toString())
                .dateTime(LocalDateTime.now())
                .message(ex.getMessage())
                .requestId(exchange.getRequest().getId())
                .build();

        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);

        DataBuffer dataBuffer = null;
        try {
            dataBuffer = exchange.getResponse().bufferFactory().wrap(objectMapper.writeValueAsBytes(errorResponse));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        if(ex instanceof SQLException sqlException){
            exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
        }

        System.out.println("Hi"+ex.getMessage());

        return exchange.getResponse().writeWith(Mono.just(dataBuffer));

    }
}
