package com.lfhardware.authorization.policy.service;

import com.lfhardware.keycloak.KeycloakProtectionAdapter;
import org.keycloak.representations.idm.authorization.AbstractPolicyRepresentation;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class PolicyService implements IPolicyService {

    private final KeycloakProtectionAdapter keycloakProtectionAdapter;

    public PolicyService(KeycloakProtectionAdapter keycloakProtectionAdapter) {
        this.keycloakProtectionAdapter = keycloakProtectionAdapter;
    }

    @Override
    public Mono<AbstractPolicyRepresentation> create(String resourceId, AbstractPolicyRepresentation policyRepresentation){
        return keycloakProtectionAdapter.createPolicy(resourceId, policyRepresentation);
    }

}
