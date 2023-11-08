package com.lfhardware.auth.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.stream.Collectors;

@Service
public class TokenService {

    private final JwtEncoder jwtEncoder;

    public TokenService(@Qualifier("accessTokenEncoder") JwtEncoder jwtEncoder){
        this.jwtEncoder = jwtEncoder;
    }

    public Mono<String> generateAccessToken(Authentication authentication){

        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder().issuedAt(now).subject(authentication.getPrincipal().toString())
                .expiresAt(now.plus(20, ChronoUnit.DAYS)).build();

        System.out.println("Hi");

        return Mono.just(jwtEncoder
                .encode(JwtEncoderParameters
                        .from(JwsHeader.with(MacAlgorithm.HS256).header("typ", "JWT").build(), claims))
                .getTokenValue());
    }
}
