package com.lfhardware.authorization.policy.factory;

import org.keycloak.representations.idm.authorization.AbstractPolicyRepresentation;
import org.keycloak.representations.idm.authorization.UserPolicyRepresentation;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class UserPolicyRepresentationFactory implements PolicyRepresentationFactory<String> {


    @Override
    public AbstractPolicyRepresentation createPolicyRepresentation(String resourceId, Set<String> scopes, String description, String name,
                                                                   Set<String> userIds) {
        UserPolicyRepresentation userPolicyRepresentation = new UserPolicyRepresentation();
        userPolicyRepresentation.setName(name);
        userPolicyRepresentation.setDescription(description);
        userPolicyRepresentation.setScopes(scopes);
        userPolicyRepresentation.setUsers(userIds);
        return userPolicyRepresentation;
    }
}
