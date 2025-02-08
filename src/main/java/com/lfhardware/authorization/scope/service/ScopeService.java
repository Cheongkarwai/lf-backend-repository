package com.lfhardware.authorization.scope.service;

import com.lfhardware.keycloak.KeycloakAdminAdapter;
import com.lfhardware.keycloak.KeycloakProtectionAdapter;
import org.keycloak.representations.idm.authorization.ScopeRepresentation;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class ScopeService implements IScopeService {


    private final KeycloakProtectionAdapter keycloakProtectionAdapter;

    private final KeycloakAdminAdapter keycloakAdminAdapter;

    public ScopeService(KeycloakProtectionAdapter keycloakProtectionAdapter,
                        KeycloakAdminAdapter keycloakAdminAdapter) {
        this.keycloakProtectionAdapter = keycloakProtectionAdapter;
        this.keycloakAdminAdapter = keycloakAdminAdapter;
    }

    @Override
    public Flux<ScopeRepresentation> findAll(String first, String max, boolean deep) {
        return keycloakAdminAdapter.findAllScopes(first, max ,deep);
    }
}
