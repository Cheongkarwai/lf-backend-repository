package com.lfhardware.keycloak;

import com.lfhardware.configuration.KeycloakProperties;
import org.apache.http.HttpHeaders;
import org.keycloak.authorization.client.representation.TokenIntrospectionResponse;
import org.keycloak.representations.idm.authorization.*;
import org.keycloak.representations.oidc.TokenMetadataRepresentation;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Map;


@Component
public class KeycloakUmaAdapter extends BaseAdapter {

    private final KeycloakProperties keycloakProperties;

    public KeycloakUmaAdapter(WebClient.Builder webClientBuilder,
                              KeycloakProperties keycloakProperties) {
        super(webClientBuilder.baseUrl(String.format("%s/%s/%s/", keycloakProperties.getServerUrl(), "realms", keycloakProperties.getRealm()))
                .build());
        super.webClient = webClient.mutate().filter(errorHandlingFilter()).build();
        this.keycloakProperties = keycloakProperties;
    }

    private ExchangeFilterFunction errorHandlingFilter() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            if (clientResponse.statusCode().is4xxClientError()) {
                return clientResponse.bodyToMono(ErrorResponse.class)
                        .flatMap(errorDetails -> {
                            return Mono.error(new KeycloakClientException(errorDetails));
                        });
            }
            return Mono.just(clientResponse);
        });
    }

    public Mono<AuthorizationResponseV2> getRptToken(String token, AuthorizationRequest authorizationRequest) {

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.put(HttpHeaders.CONTENT_TYPE, List.of(MediaType.APPLICATION_FORM_URLENCODED_VALUE));
        headers.put(HttpHeaders.AUTHORIZATION, List.of("Bearer " + token));
        authorizationRequest.setAudience(keycloakProperties.getClientId());

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("audience", keycloakProperties.getClientId());
        formData.add("grant_type", "urn:ietf:params:oauth:grant-type:uma-ticket");
        formData.add("submit_request", String.valueOf(authorizationRequest.isSubmitRequest()));

        if (authorizationRequest.getTicket() != null) {
            formData.add("ticket", authorizationRequest.getTicket());
        }
        if (authorizationRequest.getMetadata() != null && authorizationRequest.getMetadata().getResponseMode() != null) {
            formData.add("response_mode", authorizationRequest.getMetadata().getResponseMode());
        }
        if (authorizationRequest.getClaimToken() != null) {
            formData.add("claim_token", authorizationRequest.getClaimToken());
        }
        if (authorizationRequest.getClaimTokenFormat() != null) {
            formData.add("claim_token_format", authorizationRequest.getClaimTokenFormat());
        }
        if (authorizationRequest.getRptToken() != null) {
            formData.add("rpt", authorizationRequest.getRptToken());
        }

        for (Permission permission : authorizationRequest.getPermissions().getPermissions()) {
            for (String scope : permission.getScopes()) {
                formData.add("permission", permission.getResourceId() + "#" + scope);
            }
        }

        return post(String.format("/%s/%s/%s", "protocol", "openid-connect", "token"),
                BodyInserters.fromFormData(formData), headers, AuthorizationResponseV2.class);
    }

    public Flux<PermissionTicketRepresentation> findAllTicket(String rptToken) {
        LinkedMultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.put(HttpHeaders.AUTHORIZATION, Collections.singletonList("Bearer " + rptToken));
        return getAll("/authz/protection/permission/ticket",
                new LinkedMultiValueMap<>(), (httpHeaders) -> httpHeaders.addAll(headers),
                PermissionTicketRepresentation.class);
    }

    public Mono<Void> deleteTicket(String resourceOwnerToken, String ticketId) {
        LinkedMultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.put(HttpHeaders.AUTHORIZATION, Collections.singletonList("Bearer " + resourceOwnerToken));
        return delete("/authz/protection/permission/ticket", headers, Void.class);
    }

    public Mono<Void> updateTicket(String ownerToken, PermissionTicketRepresentation permissionTicketRepresentation) {
        LinkedMultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.put(HttpHeaders.AUTHORIZATION, Collections.singletonList("Bearer " + ownerToken));
        return put("/authz/protection/permission/ticket", permissionTicketRepresentation, headers, Void.class);
    }

    public Mono<PermissionResponse> createTicket(String requesterToken, List<PermissionRequest> permissionRequests) {
        LinkedMultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.put(HttpHeaders.AUTHORIZATION, Collections.singletonList("Bearer " + requesterToken));
        return post("/authz/protection/permission", permissionRequests, headers, PermissionResponse.class);
    }

    public Mono<Void> createUmaPolicy(String ownerToken, String resourceId, AbstractPolicyRepresentation policyRepresentation) {
        LinkedMultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.put(HttpHeaders.AUTHORIZATION, Collections.singletonList("Bearer " + ownerToken));
        return post(String.format("%s/%s/%s/%s", "authz", "protection", "uma-policy", resourceId), policyRepresentation, headers, Void.class);
    }

    public Mono<Void> deleteUmaPolicy(String ownerToken, String permissionId) {
        LinkedMultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.put(HttpHeaders.AUTHORIZATION, Collections.singletonList("Bearer " + ownerToken));
        return delete(String.format("%s/%s/%s/%s", "authz", "protection", "uma-policy", permissionId), headers, Void.class);
    }

    public Mono<Void> updateUmaPolicy(String ownerToken, String permissionId, AbstractPolicyRepresentation policyRepresentation) {
        LinkedMultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.put(HttpHeaders.AUTHORIZATION, Collections.singletonList("Bearer " + ownerToken));
        return put(String.format("%s/%s/%s/%s", "authz", "protection", "uma-policy", permissionId), policyRepresentation, headers, Void.class);
    }

    public Flux<AbstractPolicyRepresentation> findAllUmaPolicy(String ownerToken, String resourceId,
                                                                        String name, String scope) {
        LinkedMultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.put(HttpHeaders.AUTHORIZATION, Collections.singletonList("Bearer " + ownerToken));
        LinkedMultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        if(resourceId != null){
            queryParams.add("resource", resourceId);
        }
        if(name != null){
            queryParams.add("name", name);
        }
        if(scope != null){
            queryParams.add("scope", scope);
        }
        return getAll(String.format("%s/%s/%s", "authz", "protection", "uma-policy"), queryParams, (httpHeaders) -> httpHeaders.addAll(headers), AbstractPolicyRepresentation.class);
    }

    public Mono<TokenIntrospectionResponse> introspectRptToken(String rptToken){
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("token", rptToken);
        formData.add("token_type_hint", "requesting_party_token");
        formData.add("client_id", keycloakProperties.getClientId());
        formData.add("client_secret", keycloakProperties.getClientSecret());
        BodyInserters.FormInserter<String> bodyInserter = BodyInserters.fromFormData(formData);
        return post(String.format("%s/%s/%s/%s","protocol","openid-connect", "token", "introspect"), bodyInserter, new LinkedMultiValueMap<>(), TokenIntrospectionResponse.class);
    }
}
