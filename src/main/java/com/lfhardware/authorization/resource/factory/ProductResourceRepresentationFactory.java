package com.lfhardware.authorization.resource.factory;

import com.lfhardware.authorization.resource.dto.ResourceType;
import com.lfhardware.product.dto.ProductDTO;
import org.keycloak.representations.idm.authorization.ResourceRepresentation;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class ProductResourceRepresentationFactory extends AbstractResourceRepresentationFactory<ProductDTO> {

    @Override
    public ResourceRepresentation createResourceRepresentation(Jwt jwt, ProductDTO product, Set<String> scopes) {
        String uri = "/api/v1/products/" + product.getId();
        return initializeBaseRepresentation(
                jwt,
                String.valueOf(product.getId()),
                product.getName(),
                ResourceType.PRODUCT,
                Set.of(uri),
                scopes
        );
    }
}
