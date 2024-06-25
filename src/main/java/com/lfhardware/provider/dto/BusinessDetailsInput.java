package com.lfhardware.provider.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lfhardware.auth.domain.Address;
import com.lfhardware.auth.dto.AddressDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BusinessDetailsInput {

    private String name;

    @JsonProperty("email_address")
    private String emailAddress;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("fax_no")
    private String faxNo;

    private String location;

    private BusinessAddressInput address;

    private String website;
}
