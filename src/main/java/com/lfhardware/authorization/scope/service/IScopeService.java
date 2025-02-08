package com.lfhardware.authorization.scope.service;

import org.keycloak.representations.idm.authorization.ScopeRepresentation;
import reactor.core.publisher.Flux;

public interface IScopeService {

    Flux<ScopeRepresentation> findAll(String first, String max, boolean deep);
}
