package com.lfhardware.charges.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payer {

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("email_address")
    private String emailAddress;

    private String name;

    @JsonProperty("credit_card")
    private String creditCard;
}
