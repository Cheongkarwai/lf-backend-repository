package com.lfhardware.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "keycloak.admin")
public class KeycloakProperties {

    private String grantType;

    private String username;

    private String password;

    private String clientId;

    private String clientSecret;

    private String realm;

    private String serverUrl;


}
