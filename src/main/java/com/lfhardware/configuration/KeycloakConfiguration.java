package com.lfhardware.configuration;

import lombok.NoArgsConstructor;
import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientImpl;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

@Configuration
@EnableConfigurationProperties({KeycloakProperties.class})
public class KeycloakConfiguration {

    @Autowired
    private KeycloakProperties keycloakProperties;


    @Bean
    public Keycloak keycloak(){
        return KeycloakBuilder
                .builder()
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientId(keycloakProperties.getClientId())
                .clientSecret(keycloakProperties.getClientSecret())
                .realm(keycloakProperties.getRealm())
                .serverUrl(keycloakProperties.getServerUrl())
                .build();
    }
}
