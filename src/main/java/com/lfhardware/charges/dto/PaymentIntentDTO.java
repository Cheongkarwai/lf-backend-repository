package com.lfhardware.charges.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentIntentDTO {

    private String id;
    @JsonProperty("client_secret")
    private String clientSecret;

}
