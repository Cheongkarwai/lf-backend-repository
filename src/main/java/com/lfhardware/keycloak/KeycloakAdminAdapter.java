package com.lfhardware.keycloak;

import com.lfhardware.configuration.KeycloakProperties;
import com.lfhardware.user.dto.UserDTO;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.representations.idm.authorization.ScopeRepresentation;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class KeycloakAdminAdapter extends BaseAdapter {

    private final KeycloakProperties keycloakProperties;

    public KeycloakAdminAdapter(WebClient.Builder webClientBuilder,
                                KeycloakProperties keycloakProperties,
                                KeycloakTokenAdapter keycloakAdapter) {
        super(webClientBuilder.baseUrl(String.format("%s/%s/%s/", keycloakProperties.getServerUrl(), "admin", "realms"))
                .build());
        super.webClient = webClient.mutate().filter(addBearerTokenFilter(keycloakAdapter)).build();
        this.keycloakProperties = keycloakProperties;
    }

    private ExchangeFilterFunction addBearerTokenFilter(KeycloakTokenAdapter keycloakAdapter) {
        //keycloak.realm(keycloakProperties.getRealm()).roles().get("user").getUserMembers();
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            // Call to auth server to get token
            return keycloakAdapter.grantToken().map(token -> {
                return ClientRequest.from(clientRequest)  // Clone existing request
                        .header("Authorization", "Bearer " + token.getToken()) // Add Bearer token header
                        .build();
            });
        });
    }

    public Flux<UserRepresentation> findAllUsers(Boolean briefRepresentation,
                                                 String email, Boolean emailVerified,
                                                 Boolean enabled, Boolean exact, String first,
                                                 String firstName, String idpAlias,
                                                 String lastName, String max,
                                                 String q, String search, String username) {

        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        if (briefRepresentation != null) queryParams.add("briefRepresentation", briefRepresentation.toString());
        if (email != null) queryParams.add("email", email);
        if (emailVerified != null) queryParams.add("emailVerified", emailVerified.toString());
        if (enabled != null) queryParams.add("enabled", enabled.toString());
        if (exact != null) queryParams.add("exact", exact.toString());
        if (first != null) queryParams.add("first", first);
        if (firstName != null) queryParams.add("firstName", firstName);
        if (idpAlias != null) queryParams.add("idpAlias", idpAlias);
        if (lastName != null) queryParams.add("lastName", lastName);
        if (max != null) queryParams.add("max", max);
        if (q != null) queryParams.add("q", q);
        if (search != null) queryParams.add("search", search);
        if (username != null) queryParams.add("username", username);


        return getAll(String.format("%s/users", keycloakProperties.getRealm()), queryParams, (httpHeaders) -> httpHeaders.addAll(new LinkedMultiValueMap<>()), UserRepresentation.class);
    }

    public Mono<UserRepresentation> findUserById(String id, boolean userProfileMetadata) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        if (userProfileMetadata) queryParams.add("userProfileMetadata", "true");

        return get(String.format("%s/users", keycloakProperties.getRealm()), queryParams, (httpHeaders) -> httpHeaders.addAll(new LinkedMultiValueMap<>()), UserRepresentation.class);
    }

    public Mono<Void> createUser(UserRepresentation userRepresentation) {
        return post(String.format("%s/users", keycloakProperties.getRealm()),
                userRepresentation,
                new LinkedMultiValueMap<>(),
                Void.class);
    }

    public Mono<Void> assignRealmRoles(String userId, List<RoleRepresentation> roleRepresentations) {
        return post(String.format("%s/users/%s/role-mappings/realm", keycloakProperties.getRealm(), userId),
                roleRepresentations,
                new LinkedMultiValueMap<>(),
                Void.class);
    }

    public Flux<RoleRepresentation> findAllUserRealmRolesAvailable(String userId) {
        return getAll(String.format("%s/users/%s/role-mappings/realm/available", keycloakProperties.getRealm(),
                        userId), new LinkedMultiValueMap<>(),
                (httpHeaders) -> httpHeaders.addAll(new LinkedMultiValueMap<>()),
                RoleRepresentation.class);
    }

    public Mono<UserDTO> updateUser(String id, UserRepresentation userRepresentation) {
        return null;
    }

    public Flux<ScopeRepresentation> findAllScopes(String first, String max, boolean deep) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        if (first != null) {
            queryParams.add("first", first);
        }
        if (max != null) {
            queryParams.add("max", max);
        }
        queryParams.add("deep", String.valueOf(deep));
        return getAll(String.format("%s/clients/%s/authz/resource-server/scope", keycloakProperties.getRealm(), keycloakProperties.getId()),
                queryParams, (httpHeaders) -> httpHeaders.addAll(new LinkedMultiValueMap<>()), ScopeRepresentation.class);
    }
}
