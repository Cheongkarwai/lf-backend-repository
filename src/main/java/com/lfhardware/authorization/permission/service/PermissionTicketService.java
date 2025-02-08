package com.lfhardware.authorization.permission.service;

import com.lfhardware.keycloak.KeycloakUmaAdapter;
import org.keycloak.representations.idm.authorization.PermissionRequest;
import org.keycloak.representations.idm.authorization.PermissionResponse;
import org.keycloak.representations.idm.authorization.PermissionTicketRepresentation;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class PermissionTicketService implements IPermissionTicketService {

    private final KeycloakUmaAdapter keycloakUmaAdapter;

    public PermissionTicketService(KeycloakUmaAdapter keycloakUmaAdapter) {
        this.keycloakUmaAdapter = keycloakUmaAdapter;
    }

    @Override
    public Mono<PermissionResponse> create(String token, List<PermissionRequest> permissionRequests) {
        return keycloakUmaAdapter.createTicket(token, permissionRequests);
    }

    @Override
    public Mono<Void> delete(String token, String ticketId) {
        return keycloakUmaAdapter.deleteTicket(token, ticketId);
    }

    @Override
    public Mono<Void> update(String token, PermissionTicketRepresentation permissionTicketRepresentation) {
        return keycloakUmaAdapter.updateTicket(token, permissionTicketRepresentation);
    }
}
