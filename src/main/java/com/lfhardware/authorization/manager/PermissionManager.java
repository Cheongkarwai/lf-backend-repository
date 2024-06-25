package com.lfhardware.authorization.manager;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;


@Component
public class PermissionManager {

    public Mono<Authentication> getAuthentication(){
        return ReactiveSecurityContextHolder.getContext().map(SecurityContext::getAuthentication);
    }

    public Mono<String> getName(){
        return ReactiveSecurityContextHolder.getContext().map(SecurityContext::getAuthentication)
                .flatMap(authentication ->  Mono.just(authentication.getName()));
    }

    public Mono<List<String>> getAuthorities(){
        return ReactiveSecurityContextHolder.getContext().map(SecurityContext::getAuthentication)
                .flatMap(authentication -> Mono.just(authentication.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList())));
    }

    public Mono<Boolean> isAuthenticated(){
        return ReactiveSecurityContextHolder.getContext().map(SecurityContext::getAuthentication)
                .map(Authentication::isAuthenticated);
    }



}
