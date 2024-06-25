package com.lfhardware.keycloak.rest.account.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InvalidOTPException extends RuntimeException{

    private String error;

    @JsonProperty("error_description")
    private String errorDescription;

    private String message;

    public InvalidOTPException(String message){
        super(message);
    }
}
