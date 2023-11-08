package com.lfhardware.config;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.ReactiveAuthenticationManagerAdapter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.web.access.server.BearerTokenServerAccessDeniedHandler;
import org.springframework.security.oauth2.server.resource.web.server.BearerTokenServerAuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import reactor.core.publisher.Mono;

import javax.crypto.spec.SecretKeySpec;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {


    @Bean
    SecurityWebFilterChain securityFilterChain(ServerHttpSecurity httpSecurity){
        return httpSecurity
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(authorizeExchangeSpec -> authorizeExchangeSpec
                        .pathMatchers("/api/v1/auth/**","/**").permitAll()
                        .anyExchange().authenticated())
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .oauth2ResourceServer(oAuth2ResourceServerSpec -> oAuth2ResourceServerSpec.jwt(Customizer.withDefaults()))
                .exceptionHandling(exceptionHandlingSpec -> exceptionHandlingSpec.authenticationEntryPoint(new BearerTokenServerAuthenticationEntryPoint())
                        .accessDeniedHandler(new BearerTokenServerAccessDeniedHandler()))
                .build();
    }

//    @Bean
//    public MapReactiveUserDetailsService userDetailsService() {
//        UserDetails user = User.builder()
//                .username("user")
//                .password("{noop}user")
//                .roles("USER")
//                .build();
//        UserDetails admin = User.builder()
//                .username("admin")
//                .password("{noop}admin")
//                .roles("ADMIN")
//                .build();
//        return new MapReactiveUserDetailsService(user, admin);
//    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public ReactiveAuthenticationManager reactiveAuthenticationManager(){
//        return authentication->{
//
//            System.out.println(authentication);
//
//            reactiveUserDetailsService.findByUsername(authentication.getPrincipal().toString());
//
//
////            String scope = userDetails.getAuthorities().stream().map(Object::toString).collect(Collectors.joining(" "));
////
////            Instant now = Instant.now();
////
////            JwtClaimsSet claims = JwtClaimsSet.builder().issuedAt(now).subject(userDetails.getUsername())
////                    .claim("jwt_acl", Arrays.asList("ADMIN"))
////                    .expiresAt(now.plus(20, ChronoUnit.DAYS)).claim("scope", scope).build();
////
////            return accessTokenEncoder
////                    .encode(JwtEncoderParameters
////                            .from(JwsHeader.with(() -> MacAlgorithm.HS256.getName()).header("typ", "JWT").build(), claims))
////                    .getTokenValue();
//
//
//            return Mono.just(new UsernamePasswordAuthenticationToken(authentication.getPrincipal(),authentication.getCredentials()));
//
//        };
//    }

//    @Bean
//    public DaoAuthenticationProvider daoAuthenticationProvider(){
//       DaoAu daoAuthenticationProvider =  new DaoAuthenticationProvider();
//       daoAuthenticationProvider.setUserDetailsService(userDetailsService());
//    }

    @Bean
    JwtEncoder refreshTokenEncoder() {
        SecretKeySpec secretKeySpec = new SecretKeySpec("ttestttttttttttttttttttttttttt".getBytes(),
                MacAlgorithm.HS256.getName());
        JWKSource<SecurityContext> jwks = new ImmutableSecret<>(secretKeySpec);
        return new NimbusJwtEncoder(jwks);
    }

    @Bean
    ReactiveJwtDecoder refreshTokenDecoder() {
        return NimbusReactiveJwtDecoder.withSecretKey(
                        new SecretKeySpec("ttesttttttttttttttttttttttttttassssssssssssssssssssssssssssssss".getBytes(), MacAlgorithm.HS256.getName()))
                .macAlgorithm(MacAlgorithm.HS256).build();
    }

    // encoder for access token
    @Bean
    @Primary
    JwtEncoder accessTokenEncoder() {
        SecretKeySpec secretKeySpec = new SecretKeySpec("ttesttttttttttttttttttttttttttaaaaaaaaaaaaaaaaaaaaaaaaaaaaasssssssssssssssssss".getBytes(),
                MacAlgorithm.HS256.getName());
        JWKSource<SecurityContext> jwks = new ImmutableSecret<>(secretKeySpec);
        return new NimbusJwtEncoder(jwks);
    }

    @Bean
    @Primary
    ReactiveJwtDecoder accessTokenDecoder() {
        return NimbusReactiveJwtDecoder.withSecretKey(
                        new SecretKeySpec("ttesttttttttttttttttttttttttttaaaaaaaaaaaaaaaaaaaaaaaaaaaaasssssssssssssssssss".getBytes(), MacAlgorithm.HS256.getName()))
                .build();
    }
}
