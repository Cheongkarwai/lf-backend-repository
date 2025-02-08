package com.lfhardware.authorization.resource.factory;

import org.keycloak.representations.idm.authorization.ResourceRepresentation;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Set;

public abstract class AbstractResourceRepresentationFactory<T> implements ResourceRepresentationFactory<T> {

    protected ResourceRepresentation initializeBaseRepresentation(Jwt jwt, String id, String name, String type, Set<String> uris,
                                                                  Set<String> scopes) {
        ResourceRepresentation resourceRepresentation = new ResourceRepresentation();
        resourceRepresentation.setId(id);
        resourceRepresentation.setOwner(jwt.getSubject());
        resourceRepresentation.setOwnerManagedAccess(true);
        resourceRepresentation.setDisplayName(name);
        resourceRepresentation.setName(id);
        resourceRepresentation.setType(type);
        resourceRepresentation.setUris(uris);
        for (String scope : scopes) {
            resourceRepresentation.addScope(scope);
        }
        return resourceRepresentation;
    }

}
