package com.lfhardware.auth.api;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class PermissionApi {


    public PermissionApi(){

    }

    public Mono<ServerResponse> createPermission(ServerRequest serverRequest){

//        return ReactiveSecurityContextHolder.getContext().flatMap(authentication->{
//
//
//        });
        return ServerResponse.ok().build();
    }
}
