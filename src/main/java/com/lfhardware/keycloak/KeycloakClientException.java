package com.lfhardware.keycloak;

import lombok.Data;

@Data
public class KeycloakClientException extends RuntimeException{

    private ErrorResponse errorResponse;

    public KeycloakClientException(ErrorResponse errorResponse) {
        this.errorResponse = errorResponse;
    }
}
