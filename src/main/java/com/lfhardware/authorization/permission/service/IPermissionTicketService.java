package com.lfhardware.authorization.permission.service;

import org.keycloak.representations.idm.authorization.PermissionRequest;
import org.keycloak.representations.idm.authorization.PermissionResponse;
import org.keycloak.representations.idm.authorization.PermissionTicketRepresentation;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IPermissionTicketService {

    Mono<PermissionResponse> create(String token, List<PermissionRequest> permissionRequests);

    Mono<Void> delete(String token, String ticketId);

    Mono<Void> update(String token, PermissionTicketRepresentation permissionTicketRepresentation);
}
