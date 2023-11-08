package com.lfhardware.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Mono;

import java.util.List;

@Configuration
public class SecurityDelegationConfiguration {

    private ReactiveUserDetailsService reactiveUserDetailsService;
    private PasswordEncoder passwordEncoder;

    public SecurityDelegationConfiguration(ReactiveUserDetailsService reactiveUserDetailsService,PasswordEncoder passwordEncoder){
        this.reactiveUserDetailsService = reactiveUserDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public ReactiveAuthenticationManager reactiveAuthenticationManager() {
        return authentication -> reactiveUserDetailsService.findByUsername(authentication.getPrincipal().toString())
                .switchIfEmpty(Mono.error(new BadCredentialsException("User not found")))
                .flatMap(e->{
                    System.out.println(authentication.getCredentials().toString());
                    if(passwordEncoder.matches(authentication.getCredentials().toString(),e.getPassword())){
                        return Mono.just(new UsernamePasswordAuthenticationToken(authentication.getPrincipal(),authentication.getCredentials(), List.of(new SimpleGrantedAuthority("Hi"))));
                    }
                    return Mono.error(new BadCredentialsException("Invalid username and password"));
                });
    }
}
