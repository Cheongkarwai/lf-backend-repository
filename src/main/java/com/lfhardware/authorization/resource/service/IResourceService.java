package com.lfhardware.authorization.resource.service;

import org.keycloak.representations.idm.authorization.ResourceRepresentation;
import reactor.core.publisher.Mono;

public interface IResourceService {

    Mono<Void> create(ResourceRepresentation resourceRepresentation);

    Mono<Void> delete(String resourceId);

    Mono<Void> update(String resourceId, ResourceRepresentation resourceRepresentation);
}
