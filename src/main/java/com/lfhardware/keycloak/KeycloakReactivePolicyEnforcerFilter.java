package com.lfhardware.keycloak;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.keycloak.representations.idm.authorization.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class KeycloakReactivePolicyEnforcerFilter implements WebFilter {

    private static final List<String> PUBLIC_ROUTE = List.of("/", "/api/v1/payments/webhook", "/api/v1/services", "/api/v1/service-providers",
            "/api/v1/service-providers/details/{id}", "/api/v1/service-providers/{id}/reviews", "/api/v1/users/me/username",
            "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/swagger-resources/**", "/swagger-resources", "/api-docs/**", "/test");

    private final KeycloakProtectionAdapter keycloakAuthorizationAdapter;

    private final KeycloakUmaAdapter keycloakUmaAdapter;

    public KeycloakReactivePolicyEnforcerFilter(KeycloakProtectionAdapter keycloakAuthorizationAdapter,
                                                KeycloakUmaAdapter keycloakUmaAdapter) {
        this.keycloakAuthorizationAdapter = keycloakAuthorizationAdapter;
        this.keycloakUmaAdapter = keycloakUmaAdapter;
    }

    @NotNull
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, @NotNull WebFilterChain chain) {

        if(PUBLIC_ROUTE.contains(exchange.getRequest().getURI().getPath())){
            return chain.filter(exchange);
        }

        return ReactiveSecurityContextHolder.getContext()
                .switchIfEmpty(Mono.defer(() -> Mono.error(new AccessDeniedException("Missing jwt token"))))
                .flatMap(context -> {
                    if (context.getAuthentication().getCredentials() instanceof Jwt jwtToken) {
                        System.out.println(exchange.getRequest().getURI().getPath());
                        return keycloakAuthorizationAdapter.findResourceSet(exchange.getRequest().getURI().getPath(), true)
                                .collectList()
                                .flatMap(resourceRepresentations -> authorize(jwtToken.getTokenValue(), new AuthorizationRequestV2(), resourceRepresentations)
                                        .flatMap(response -> {
                                            System.out.println(response.getToken());
                                            if (response.getError() != null) {
                                                return exchange.getResponse().writeWith(Mono.error(new AccessDeniedException(response.getError())));
                                            }
                                            return chain.filter(exchange);
                                        }));
                    }
                    return exchange.getResponse().writeWith(Mono.error(new AccessDeniedException("Missing jwt token")));
                }).onErrorResume(AccessDeniedException.class, e -> exchange.getResponse().writeWith(Mono.error(e)));
    }

    private Mono<AuthorizationResponseV2> authorize(String tokenValue, AuthorizationRequest authorizationRequest,
                                                    List<ResourceRepresentation> resourceRepresentations) {
        List<Permission> permissionList = resourceRepresentations.parallelStream().map(resourceRepresentation -> {
            Permission permission = new Permission();
            permission.setResourceId(resourceRepresentation.getId());
            permission.setScopes(resourceRepresentation.getScopes().parallelStream().map(ScopeRepresentation::getName).collect(Collectors.toSet()));
            return permission;
        }).toList();
        PermissionTicketToken permissionTicketToken = new PermissionTicketToken(permissionList, null, null);
        authorizationRequest.setPermissions(permissionTicketToken);
        return keycloakUmaAdapter.getRptToken(tokenValue, authorizationRequest);
    }
}
