package com.lfhardware.checkout.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lfhardware.core.dto.Currency;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceItemInput {

    @JsonProperty("service_name")
    private String serviceName;

    private Long price;

    private Currency currency;

}
