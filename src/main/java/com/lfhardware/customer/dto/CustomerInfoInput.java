package com.lfhardware.customer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerInfoInput {

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("email_address")
    private String emailAddress;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("phone_number_prefix")
    private String phoneNumberPrefix;

    private CustomerAddressInput address;

    @JsonProperty("email_verified")
    private boolean isEmailVerified;

    @JsonProperty("enabled")
    private boolean isEnabled;
}
