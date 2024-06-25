package com.lfhardware.provider.dto;

import com.fasterxml.jackson.annotation.JsonProperty;


public record BusinessDetailsDTO(String name, @JsonProperty("email_address") String emailAddress, String description,
                                 String address) {

}
