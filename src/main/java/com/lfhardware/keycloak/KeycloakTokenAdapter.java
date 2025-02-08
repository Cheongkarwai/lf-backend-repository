package com.lfhardware.keycloak;

import com.lfhardware.configuration.KeycloakProperties;
import com.nimbusds.oauth2.sdk.GrantType;
import org.apache.http.HttpHeaders;
import org.keycloak.representations.AccessTokenResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class KeycloakTokenAdapter extends BaseAdapter {

    private final KeycloakProperties keycloakProperties;

    public KeycloakTokenAdapter(WebClient.Builder webClientBuilder, KeycloakProperties keycloakProperties){
        super(webClientBuilder.baseUrl(String.format("%s/%s/",keycloakProperties.getServerUrl(),"realms")).build());
        this.keycloakProperties = keycloakProperties;
    }

    public Mono<AccessTokenResponse> grantToken(){
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.put(HttpHeaders.CONTENT_TYPE, List.of(MediaType.APPLICATION_FORM_URLENCODED_VALUE));

        return post("/"+keycloakProperties.getRealm()+"/protocol/openid-connect/token",
                BodyInserters.fromFormData("grant_type", GrantType.CLIENT_CREDENTIALS.getValue())
                        .with("client_id", keycloakProperties.getClientId())
                        .with("client_secret", keycloakProperties.getClientSecret()), headers,
                AccessTokenResponse.class);
    }
}
