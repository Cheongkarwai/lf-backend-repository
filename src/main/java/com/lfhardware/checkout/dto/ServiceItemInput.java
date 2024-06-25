package com.lfhardware.checkout.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lfhardware.shared.Currency;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceItemInput {

    @JsonProperty("service_name")
    private String serviceName;

    private BigDecimal price;

    private Currency currency;

}
