package com.lfhardware.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({KeycloakProperties.class})
public class KeycloakConfiguration {

    @Autowired
    private KeycloakProperties keycloakProperties;


//    @Bean
//    public Keycloak keycloak(){
//        return KeycloakBuilder
//                .builder()
//                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
//                .clientId(keycloakProperties.getClientId())
//                .clientSecret(keycloakProperties.getClientSecret())
//                .realm(keycloakProperties.getRealm())
//                .serverUrl(keycloakProperties.getServerUrl())
//                .build();
//    }
}
