package com.lfhardware.order.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lfhardware.auth.dto.AddressDTO;

public record RecipientDTO(@JsonProperty("first_name") String firstName, @JsonProperty("last_name") String lastName,
                           @JsonProperty("phone_number") String phoneNumber, @JsonProperty("email_address") String emailAddress,
                           @JsonProperty("delivery_address") AddressDTO address){}
