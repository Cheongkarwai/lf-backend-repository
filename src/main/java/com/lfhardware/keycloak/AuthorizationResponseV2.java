package com.lfhardware.keycloak;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.keycloak.representations.idm.authorization.AuthorizationResponse;

@EqualsAndHashCode(callSuper = true)
@Data
public class AuthorizationResponseV2 extends AuthorizationResponse {

    private boolean result;

    public enum ResponseMode{
        permissions,
        decision
    }
}
