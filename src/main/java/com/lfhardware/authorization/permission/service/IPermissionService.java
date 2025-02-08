package com.lfhardware.authorization.permission.service;

import org.keycloak.representations.idm.authorization.AbstractPolicyRepresentation;
import org.keycloak.representations.idm.authorization.PermissionRequest;
import org.keycloak.representations.idm.authorization.PermissionTicketRepresentation;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IPermissionService {

    Mono<Void> submitRequest(String requesterToken, List<PermissionRequest> permissionRequests, boolean submitRequest);

    Mono<Void> grant(String requesterToken, PermissionTicketRepresentation permissionTicketRepresentation);

    Mono<Void> revoke(String requesterToken, PermissionTicketRepresentation permissionTicketRepresentation);

    Mono<Void> createPolicy(String resourceId, AbstractPolicyRepresentation abstractPolicyRepresentation);

    Flux<AbstractPolicyRepresentation> findPolicies(String resourceId, String name, String scope);

    Mono<Void> deletePolicy(String permissionId);

    Mono<Void> updatePolicy(String permissionId, AbstractPolicyRepresentation abstractPolicyRepresentation);

}
