package com.lfhardware.auth.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lfhardware.provider.domain.ServiceDetails;
import com.lfhardware.provider.dto.BusinessDetailsInput;
import com.lfhardware.provider.dto.ServiceDetailsInput;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BasicInformation {

    @JsonProperty("business_details")
    private BusinessDetailsInput businessDetails;

    @JsonProperty("service_details")
    private ServiceDetailsInput serviceDetails;
}
