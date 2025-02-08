package com.lfhardware.authorization.permission.service;

import com.lfhardware.keycloak.ErrorResponse;
import com.lfhardware.keycloak.KeycloakClientException;
import com.lfhardware.keycloak.KeycloakUmaAdapter;
import org.keycloak.representations.idm.authorization.AbstractPolicyRepresentation;
import org.keycloak.representations.idm.authorization.AuthorizationRequest;
import org.keycloak.representations.idm.authorization.PermissionRequest;
import org.keycloak.representations.idm.authorization.PermissionTicketRepresentation;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class PermissionService implements IPermissionService {

    private final KeycloakUmaAdapter keycloakUmaAdapter;

    private final IPermissionTicketService permissionTicketService;

    public PermissionService(KeycloakUmaAdapter keycloakUmaAdapter,
                             IPermissionTicketService permissionTicketService) {
        this.keycloakUmaAdapter = keycloakUmaAdapter;
        this.permissionTicketService = permissionTicketService;
    }

    @Override
    public Mono<Void> submitRequest(String requesterToken, List<PermissionRequest> permissionRequests, boolean submitRequest) {
        return permissionTicketService.create(requesterToken, permissionRequests)
                .flatMap(permissionResponse -> {
                    AuthorizationRequest authorizationRequest = new AuthorizationRequest();
                    authorizationRequest.setTicket(permissionResponse.getTicket());
                    authorizationRequest.setSubmitRequest(submitRequest);
                    return keycloakUmaAdapter.getRptToken(requesterToken, authorizationRequest)
                            .flatMap(authorizationResponseV2 -> {
                                if (authorizationResponseV2.getError() != null) {
                                    ErrorResponse errorResponse = new ErrorResponse();
                                    errorResponse.setError(authorizationResponseV2.getError());
                                    errorResponse.setErrorDescription(authorizationResponseV2.getErrorDescription());
                                    throw new KeycloakClientException(errorResponse);
                                }
                                return Mono.empty();
                            });
                });
    }

    @Override
    public Mono<Void> grant(String requesterToken, PermissionTicketRepresentation permissionTicketRepresentation) {
        permissionTicketRepresentation.setGranted(true);
        return permissionTicketService.update(requesterToken, permissionTicketRepresentation);
    }

    @Override
    public Mono<Void> revoke(String requesterToken, PermissionTicketRepresentation permissionTicketRepresentation) {
        permissionTicketRepresentation.setGranted(false);
        return permissionTicketService.update(requesterToken, permissionTicketRepresentation);
    }

    @Override
    public Mono<Void> createPolicy(String resourceId, AbstractPolicyRepresentation policyRepresentation) {
        return ReactiveSecurityContextHolder.getContext()
                .switchIfEmpty(Mono.defer(() -> Mono.error(new AccessDeniedException("No Security Context Found"))))
                .flatMap(context -> {
                    if (context.getAuthentication() != null && context.getAuthentication().getCredentials() instanceof Jwt jwt) {
                        return keycloakUmaAdapter.createUmaPolicy(jwt.getTokenValue(), resourceId, policyRepresentation);
                    }
                    return Mono.error(new AccessDeniedException("No Security Context Found"));
                });
    }

    @Override
    public Flux<AbstractPolicyRepresentation> findPolicies(String resourceId, String name, String scope) {
        return ReactiveSecurityContextHolder.getContext()
                .switchIfEmpty(Mono.defer(() -> Mono.error(new AccessDeniedException("No Security Context Found"))))
                .flatMapMany(context -> {
                    if (context.getAuthentication() != null && context.getAuthentication().getCredentials() instanceof Jwt jwt) {
                        return keycloakUmaAdapter.findAllUmaPolicy(jwt.getTokenValue(), resourceId, name, scope);
                    }
                    return Mono.error(new AccessDeniedException("No Security Context Found"));
                });
    }

    @Override
    public Mono<Void> deletePolicy(String permissionId) {
        return ReactiveSecurityContextHolder.getContext()
                .switchIfEmpty(Mono.defer(() -> Mono.error(new AccessDeniedException("No Security Context Found"))))
                .flatMap(context -> {
                    if (context.getAuthentication() != null && context.getAuthentication().getCredentials() instanceof Jwt jwt) {
                        return keycloakUmaAdapter.deleteUmaPolicy(jwt.getTokenValue(), permissionId);
                    }
                    return Mono.error(new AccessDeniedException("No Security Context Found"));
                });
    }

    @Override
    public Mono<Void> updatePolicy(String permissionId, AbstractPolicyRepresentation abstractPolicyRepresentation) {
        return ReactiveSecurityContextHolder.getContext()
                .switchIfEmpty(Mono.defer(() -> Mono.error(new AccessDeniedException("No Security Context Found"))))
                .flatMap(context -> {
                    if (context.getAuthentication() != null && context.getAuthentication().getCredentials() instanceof Jwt jwt) {
                        return keycloakUmaAdapter.updateUmaPolicy(jwt.getTokenValue(), permissionId, abstractPolicyRepresentation);
                    }
                    return Mono.error(new AccessDeniedException("No Security Context Found"));
                });
    }


}
