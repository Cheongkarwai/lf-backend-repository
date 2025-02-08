package com.lfhardware.authorization.policy.service;

import com.lfhardware.keycloak.KeycloakUmaAdapter;
import org.springframework.stereotype.Service;

@Service
public class UmaPolicyService{

    private KeycloakUmaAdapter keycloakUmaAdapter;

    public UmaPolicyService(KeycloakUmaAdapter keycloakUmaAdapter) {
        this.keycloakUmaAdapter = keycloakUmaAdapter;
    }


}
