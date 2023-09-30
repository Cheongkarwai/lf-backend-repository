package com.lfhardware.config;

import com.lfhardware.charges.handler.PaymentHandler;
import com.lfhardware.product.handler.ProductHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.ReactivePageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.function.server.*;
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver;

import java.util.Arrays;
import java.util.List;

@Configuration
public class ReactiveWebConfig {

    @Bean
    public RouterFunction<ServerResponse> productRouter(ProductHandler productHandler){
        return RouterFunctions.route()
                        .path("/api/v1/products",
                                builder -> builder.nest(RequestPredicates.accept(MediaType.APPLICATION_JSON),
                                route->route
                                        .GET("/{id}",productHandler::findById)
                                        .GET(productHandler::findAll)
                                        .POST(productHandler::save)
                                        .PUT("/{id}",productHandler::updateById)))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> paymentRouter(PaymentHandler paymentHandler){
        return RouterFunctions.route()
                .path("/api/v1/charges",
                        builder->builder.nest(RequestPredicates.accept(MediaType.APPLICATION_JSON),
                                route->route.POST(paymentHandler::charge)
                                            .GET("/supported-payment-methods",paymentHandler::findAllPaymentMethods)
                                            .GET("/{paymentMethod}/supported-banks",paymentHandler::findAllAvailableBank)))
                .build();
    }

    @Bean
    HandlerMethodArgumentResolver reactivePageableHandlerMethodArgumentResolver() {
        return new ReactivePageableHandlerMethodArgumentResolver();
    }

    @Bean
    CorsWebFilter corsWebFilter(){
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowedMethods(List.of("*"));
        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return new CorsWebFilter(source);
    }

//    @Bean
//    WebExceptionHandler exceptionHandler(){
//        return (ServerWebExchange exchange,Throwable ex)->{
//
//            System.out.println(ex instanceof NoSuchElementException);
//
//          if(ex instanceof NoSuchElementException){
//              exchange.getResponse().setStatusCode(HttpStatus.NOT_FOUND);
//              return exchange.getResponse().setComplete();
//          }
//          return Mono.error(ex);
//        };
//    }


}
