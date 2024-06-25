package com.lfhardware.keycloak.rest.account.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OTPSetupException extends RuntimeException{

    private String error;

    @JsonProperty("error_description")
    private String errorDescription;

    public OTPSetupException(String message){
        super(message);
    }
}
