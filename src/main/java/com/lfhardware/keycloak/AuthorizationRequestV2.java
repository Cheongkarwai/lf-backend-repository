package com.lfhardware.keycloak;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.keycloak.representations.idm.authorization.AuthorizationRequest;

@EqualsAndHashCode(callSuper = true)
@Data
public class AuthorizationRequestV2 extends AuthorizationRequest {

    private Boolean submitRequest;
}
