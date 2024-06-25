package com.lfhardware.provider.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lfhardware.appointment.dto.ServiceDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceProviderDetailsInput {

    private String name;

    @JsonProperty("email_address")
    private String emailAddress;

    @JsonProperty("is_verified")
    private boolean isVerified;

    @JsonProperty("contact_info")
    private ContactInfoDTO contactInfo;

    private List<ServiceInput> services;

    private String overview;

    private CoverageInput coverage;

    private BusinessAddressInput address;


}
