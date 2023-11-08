package com.lfhardware.config;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module;
import com.lfhardware.auth.handler.AuthHandler;
import com.lfhardware.auth.handler.UserHandler;
import com.lfhardware.cart.handler.CartHandler;
import com.lfhardware.charges.handler.PaymentHandler;
import com.lfhardware.product.handler.ProductHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
    public RouterFunction<ServerResponse> cartRouter(CartHandler cartHandler){
        return RouterFunctions.route()
                .path("/api/v1",
                        builder -> builder.nest(RequestPredicates.accept(MediaType.APPLICATION_JSON),
                                route->route.POST("/users/{username}/cart/items",cartHandler::addCartItem)))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> authRouter(AuthHandler authHandler){
        return RouterFunctions.route()
                .path("/api/v1/auth",
                        builder -> builder.nest(RequestPredicates.accept(MediaType.APPLICATION_JSON),
                                route->route.POST("/login",authHandler::login)
                                        .POST("/register",authHandler::register)
                                        .POST("/account-recovery-email",authHandler::sendAccountRecoveryEmail)
                                        .POST("/change-password",authHandler::changePassword)))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> userRouter(UserHandler userHandler){
        return RouterFunctions.route()
                .path("/api/v1/users",
                        builder -> builder.nest(RequestPredicates.accept(MediaType.APPLICATION_JSON),
                                route->route.GET("/{username}", userHandler::findUser)
                                        .GET("/{username}/roles",userHandler::findUserRole)))

                .build();
    }

//    @Bean
//    HandlerMethodArgumentResolver reactivePageableHandlerMethodArgumentResolver() {
//        return new ReactivePageableHandlerMethodArgumentResolver();
//    }

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

    @Bean
    public Module datatypeHibernateModule() {
        return new Hibernate6Module();
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
