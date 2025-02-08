package com.lfhardware.authorization.policy.factory;

import org.keycloak.representations.idm.authorization.AbstractPolicyRepresentation;

import java.util.Set;

public interface PolicyRepresentationFactory<T> {

    AbstractPolicyRepresentation createPolicyRepresentation(String resourceId,
                                                            Set<String> scopes,
                                                            String description,
                                                            String name,
                                                            Set<T> values);
}
