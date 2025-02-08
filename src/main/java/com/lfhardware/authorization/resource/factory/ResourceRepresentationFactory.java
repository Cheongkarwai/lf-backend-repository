package com.lfhardware.authorization.resource.factory;

import org.keycloak.representations.idm.authorization.ResourceRepresentation;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Set;

public interface ResourceRepresentationFactory<T> {

    ResourceRepresentation createResourceRepresentation(Jwt resourceOwnerToken, T resource, Set<String> scopes);
}
