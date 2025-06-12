package com.lfhardware.keycloak;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class KeycloakClientException extends RuntimeException{

    private ErrorResponse errorResponse;

    public KeycloakClientException(ErrorResponse errorResponse) {
        this.errorResponse = errorResponse;
    }
}
