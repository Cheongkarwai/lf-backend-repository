package com.lfhardware.authorization.resource.factory;

import org.keycloak.representations.idm.authorization.ResourceRepresentation;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Set;

public interface ResourceRepresentationFactory {

    ResourceRepresentation createResourceRepresentation(Jwt resourceOwnerToken, String id, String name, Set<String> scopes);
}
