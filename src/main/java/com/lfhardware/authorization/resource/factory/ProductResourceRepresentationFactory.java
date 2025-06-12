package com.lfhardware.authorization.resource.factory;

import com.lfhardware.authorization.resource.dto.ResourceType;
import org.keycloak.representations.idm.authorization.ResourceRepresentation;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class ProductResourceRepresentationFactory extends AbstractResourceRepresentationFactory {

    @Override
    public ResourceRepresentation createResourceRepresentation(Jwt jwt, String id, String name, Set<String> scopes) {
        String uri = "/api/v1/products/" + id;
        return initializeBaseRepresentation(
                jwt,
                String.valueOf(id),
                name,
                ResourceType.PRODUCT,
                Set.of(uri),
                scopes
        );
    }
}
