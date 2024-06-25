package com.lfhardware.order.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourierInput {

    @JsonProperty("service_id")
    private String serviceId;

    @JsonProperty("courier_id")
    private String courierId;

    @JsonProperty("courier_name")
    private String courierName;

    @JsonProperty("courier_logo")
    private String courierLogo;

    private BigDecimal fees;
}
