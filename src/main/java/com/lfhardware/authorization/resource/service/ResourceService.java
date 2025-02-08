package com.lfhardware.authorization.resource.service;

import com.lfhardware.keycloak.KeycloakProtectionAdapter;
import org.keycloak.representations.idm.authorization.ResourceRepresentation;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class ResourceService implements IResourceService {

    private final KeycloakProtectionAdapter keycloakProtectionAdapter;

    public ResourceService(KeycloakProtectionAdapter keycloakProtectionAdapter) {
        this.keycloakProtectionAdapter = keycloakProtectionAdapter;
    }

    public Flux<ResourceRepresentation> findAll(String uri, boolean match){
        return keycloakProtectionAdapter.findResourceSet(uri, match);
    }

    @Override
    public Mono<Void> create(ResourceRepresentation resourceRepresentation) {
        return keycloakProtectionAdapter.createResource(resourceRepresentation);
    }

    @Override
    public Mono<Void> delete(String resourceId) {
        return keycloakProtectionAdapter.deleteResource(resourceId);
    }

    @Override
    public Mono<Void> update(String resourceId, ResourceRepresentation resourceRepresentation) {
        return null;
    }
}
