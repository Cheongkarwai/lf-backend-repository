package com.lfhardware.provider.dto;

import com.fasterxml.jackson.annotation.JsonProperty;


public record ContactInfoDTO(@JsonProperty("email_address") String emailAddress,
                             @JsonProperty("phone_number") String phoneNumber, String whatsapp) {

}
