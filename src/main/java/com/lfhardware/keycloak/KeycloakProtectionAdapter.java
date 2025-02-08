package com.lfhardware.keycloak;

import com.lfhardware.configuration.KeycloakProperties;
import org.keycloak.representations.idm.authorization.AbstractPolicyRepresentation;
import org.keycloak.representations.idm.authorization.ResourceRepresentation;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class KeycloakProtectionAdapter extends BaseAdapter {

    private final KeycloakProperties keycloakProperties;

    public KeycloakProtectionAdapter(WebClient.Builder webClientBuilder,
                                     KeycloakProperties keycloakProperties,
                                     KeycloakTokenAdapter keycloakAdapter) {
        super(webClientBuilder.baseUrl(String.format("%s/%s/%s/", keycloakProperties.getServerUrl(), "realms", keycloakProperties.getRealm()))
                .build());
        super.webClient = webClient.mutate().filter(addBearerTokenFilter(keycloakAdapter)).build();
        this.keycloakProperties = keycloakProperties;
    }

    private ExchangeFilterFunction addBearerTokenFilter(KeycloakTokenAdapter keycloakAdapter) {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            // Call to auth server to get token
            return keycloakAdapter.grantToken().map(token -> {
                return ClientRequest.from(clientRequest)  // Clone existing request
                        .header("Authorization", "Bearer " + token.getToken()) // Add Bearer token header
                        .build();
            });
        });
    }


    public Flux<ResourceRepresentation> findResourceSet(String uri, Boolean matchingUri) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        if (uri != null) queryParams.add("uri", uri);
        if (matchingUri != null) queryParams.add("matchingUri", matchingUri.toString());
        queryParams.add("deep", String.valueOf(true));

        return getAll(String.format("%s/%s/%s", "authz", "protection", "resource_set"),
                queryParams, (httpHeaders) -> httpHeaders.addAll(new LinkedMultiValueMap<>()), ResourceRepresentation.class);

    }

    public Flux<String> findResourceSetIds(String uri, Boolean matchingUri) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        if (uri != null) queryParams.add("uri", uri);
        if (matchingUri != null) queryParams.add("matchingUri", matchingUri.toString());
        queryParams.add("deep", String.valueOf(false));

        return getAll(String.format("%s/%s/%s", "authz", "protection", "resource_set"),
                queryParams, (httpHeaders) -> httpHeaders.addAll(new LinkedMultiValueMap<>()), String[].class)
                .flatMap(Flux::fromArray)
                .log();
    }

    public Mono<Void> createResource(ResourceRepresentation resourceRepresentation) {
        return post(String.format("%s/%s/%s", "authz", "protection", "resource_set"),
                resourceRepresentation, new LinkedMultiValueMap<>(), Void.class);
    }

    public Mono<AbstractPolicyRepresentation> createPolicy(String resourceId, AbstractPolicyRepresentation policyRepresentation) {
        return post(String.format("%s/%s/%s/%s", "authz", "resource-server", "policy", resourceId),
                policyRepresentation, new LinkedMultiValueMap<>(), AbstractPolicyRepresentation.class);
    }

    public Mono<Void> deleteResource(String resourceId) {
        return delete(String.format("%s/%s/%s/%s", "authz", "protection", "resource_set", resourceId),
                new LinkedMultiValueMap<>(), Void.class);
    }
}
