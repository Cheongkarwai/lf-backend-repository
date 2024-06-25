package com.lfhardware.provider.dto;

import com.fasterxml.jackson.annotation.JsonProperty;


public record BankDetailsDTO(String bank, @JsonProperty("full_name") String fullName,
                             @JsonProperty("account_number") String accountNumber) {

}
