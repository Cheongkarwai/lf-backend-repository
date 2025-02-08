package com.lfhardware.authorization.policy.service;

import org.keycloak.representations.idm.authorization.AbstractPolicyRepresentation;
import reactor.core.publisher.Mono;

public interface IPolicyService {

    Mono<AbstractPolicyRepresentation> create(String resourceId, AbstractPolicyRepresentation policy);
}
