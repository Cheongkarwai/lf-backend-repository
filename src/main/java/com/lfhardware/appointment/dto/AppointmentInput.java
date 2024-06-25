package com.lfhardware.appointment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lfhardware.appointment.domain.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentInput {

    @JsonProperty("service_provider_id")
    private String serviceProviderId;

    @JsonProperty("service_id")
    private Long serviceId;

    @JsonProperty("estimated_price")
    private BigDecimal estimatedPrice;

    private AppointmentStatus status;
}
