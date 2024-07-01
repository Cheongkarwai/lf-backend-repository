package com.lfhardware.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.oauth2.server.resource.web.access.server.BearerTokenServerAccessDeniedHandler;
import org.springframework.security.oauth2.server.resource.web.server.BearerTokenServerAuthenticationEntryPoint;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.security.web.server.csrf.CookieServerCsrfTokenRepository;
import org.springframework.security.web.server.csrf.CsrfToken;
import org.springframework.security.web.server.csrf.ServerCsrfTokenRequestHandler;
import org.springframework.security.web.server.csrf.XorServerCsrfTokenRequestAttributeHandler;
import org.springframework.security.web.server.savedrequest.NoOpServerRequestCache;
import org.springframework.security.web.server.util.matcher.NegatedServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity(useAuthorizationManager=true)
public class SecurityConfiguration {

    private String[] allowedPaths = {"/", "/api/v1/payments/webhook", "/api/v1/services", "/api/v1/service-providers" ,
            "/api/v1/service-providers/details/{id}", "/api/v1/service-providers/{id}/reviews", "/api/v1/users/me/username"};

    @Bean
    SecurityWebFilterChain securityFilterChain(ServerHttpSecurity httpSecurity){
        XorServerCsrfTokenRequestAttributeHandler delegate = new XorServerCsrfTokenRequestAttributeHandler();
        // Use only the handle() method of XorServerCsrfTokenRequestAttributeHandler and the
        // default implementation of resolveCsrfTokenValue() from ServerCsrfTokenRequestHandler
        ServerCsrfTokenRequestHandler requestHandler = delegate::handle;
        return httpSecurity
                .csrf(csrfSpec -> csrfSpec
                                .disable()
                        //.csrfTokenRepository(CookieServerCsrfTokenRepository.withHttpOnlyFalse()

                       // .requireCsrfProtectionMatcher(new NegatedServerWebExchangeMatcher(exchange -> ServerWebExchangeMatchers.pathMatchers("/api/v1/payments/webhook").matches(exchange)))
                        //.csrfTokenRequestHandler(requestHandler)
                )
                .cors(corsSpec -> corsSpec.configurationSource(corsConfigurationSource()))
                .requestCache(requestCacheSpec -> requestCacheSpec.requestCache(NoOpServerRequestCache.getInstance()))
                //.cors(cors-> cors.configurationSource(corsConfigurationSource()))
                .authorizeExchange(authorizeExchangeSpec -> authorizeExchangeSpec
                        .pathMatchers(allowedPaths).permitAll()
                        .anyExchange().authenticated())
//                .oauth2Login()
//
//                .and()
              .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .oauth2ResourceServer(oAuth2ResourceServerSpec -> oAuth2ResourceServerSpec.jwt(jwtSpec -> jwtSpec.jwtAuthenticationConverter(grantedAuthoritiesExtractor())))
                .exceptionHandling(exceptionHandlingSpec -> exceptionHandlingSpec.authenticationEntryPoint(new BearerTokenServerAuthenticationEntryPoint())
                        .accessDeniedHandler(new BearerTokenServerAccessDeniedHandler()))
                .build();
    }


    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.setAllowedOrigins(List.of("http://localhost:8090", "http://localhost:4200", "http://localhost:8090"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.addExposedHeader(HttpHeaders.CONTENT_DISPOSITION);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;

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

//    @Bean
//    public PasswordEncoder passwordEncoder(){
//        return new BCryptPasswordEncoder();
//    }


    Converter<Jwt, Mono<AbstractAuthenticationToken>> grantedAuthoritiesExtractor() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new GrantedAuthoritiesExtractor());
        return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
    }
    static class GrantedAuthoritiesExtractor
            implements Converter<Jwt, Collection<GrantedAuthority>> {

        public Collection<GrantedAuthority> convert(Jwt jwt) {
            Map<String,Object> realmAccess =  jwt.getClaim("realm_access");
            List<String> keycloakRoles = (List<String>) realmAccess.get("roles");

            return keycloakRoles.stream()
                    .filter(keycloakRole-> !(keycloakRole.equals("default-roles-lfhardware")
                            || keycloakRole.equals("offline_access")
                            || keycloakRole.equals("uma_authorization")))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        }
    }

    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter authenticationConverter = new JwtAuthenticationConverter();
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
        grantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
        authenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return authenticationConverter;
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

//    @Bean
//    JwtEncoder refreshTokenEncoder() {
//        SecretKeySpec secretKeySpec = new SecretKeySpec("ttestttttttttttttttttttttttttt".getBytes(),
//                MacAlgorithm.HS256.getName());
//        JWKSource<SecurityContext> jwks = new ImmutableSecret<>(secretKeySpec);
//        return new NimbusJwtEncoder(jwks);
//    }
//
//    @Bean
//    ReactiveJwtDecoder refreshTokenDecoder() {
//        return NimbusReactiveJwtDecoder.withSecretKey(
//                        new SecretKeySpec("ttesttttttttttttttttttttttttttassssssssssssssssssssssssssssssss".getBytes(), MacAlgorithm.HS256.getName()))
//                .macAlgorithm(MacAlgorithm.HS256).build();
//    }
//
//    // encoder for access token
//    @Bean
//    @Primary
//    JwtEncoder accessTokenEncoder() {
//        SecretKeySpec secretKeySpec = new SecretKeySpec("ttesttttttttttttttttttttttttttaaaaaaaaaaaaaaaaaaaaaaaaaaaaasssssssssssssssssss".getBytes(),
//                MacAlgorithm.HS256.getName());
//        JWKSource<SecurityContext> jwks = new ImmutableSecret<>(secretKeySpec);
//        return new NimbusJwtEncoder(jwks);
//    }
//
//    @Bean
//    @Primary
//    ReactiveJwtDecoder accessTokenDecoder() {
//        return NimbusReactiveJwtDecoder.withSecretKey(
//                        new SecretKeySpec("ttesttttttttttttttttttttttttttaaaaaaaaaaaaaaaaaaaaaaaaaaaaasssssssssssssssssss".getBytes(), MacAlgorithm.HS256.getName()))
//                .build();
//    }
}
