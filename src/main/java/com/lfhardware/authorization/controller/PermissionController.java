package com.lfhardware.authorization.controller;

import com.lfhardware.keycloak.KeycloakUmaAdapter;
import com.lfhardware.authorization.permission.service.IPermissionService;
import org.keycloak.representations.idm.authorization.PermissionRequest;
import org.keycloak.representations.idm.authorization.PermissionTicketRepresentation;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/permissions")
public class PermissionController {

    private final KeycloakUmaAdapter keycloakUmaAdapter;

    private final IPermissionService permissionService;

    public PermissionController(KeycloakUmaAdapter keycloakUmaAdapter, IPermissionService permissionService) {
        this.keycloakUmaAdapter = keycloakUmaAdapter;
        this.permissionService = permissionService;
    }

    @GetMapping
    public Flux<PermissionTicketRepresentation> findAll(@AuthenticationPrincipal Jwt jwt){
        return keycloakUmaAdapter.findAllTicket(jwt.getTokenValue());
    }

    @PostMapping("/tickets:submit")
    public Mono<Void> submitRequest(@AuthenticationPrincipal Jwt jwt,
                                                       @RequestBody List<PermissionRequest> permissionRequests){
        return permissionService.submitRequest(jwt.getTokenValue(), permissionRequests, true);
    }

    @PostMapping("/tickets:revoke")
    public Mono<Void> revoke(@AuthenticationPrincipal Jwt jwt,
                             @RequestBody PermissionTicketRepresentation permissionTicketRepresentation){
        return permissionService.revoke(jwt.getTokenValue(), permissionTicketRepresentation);
    }

    @PostMapping("/tickets:grant")
    public Mono<Void> grant(@AuthenticationPrincipal Jwt jwt,
                            @RequestBody PermissionTicketRepresentation permissionTicketRepresentation){
        return permissionService.grant(jwt.getTokenValue(), permissionTicketRepresentation);
    }

    @DeleteMapping("/tickets/{id}")
    public Mono<Void> delete(@AuthenticationPrincipal Jwt jwt,
                             @PathVariable String id){
        return keycloakUmaAdapter.deleteTicket(jwt.getTokenValue(), id);
    }
}
