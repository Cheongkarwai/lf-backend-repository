package com.lfhardware.appointment.dto;

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
public class AppointmentFeesInput {

    @JsonProperty("estimated_price")
    private BigDecimal estimatedPrice;
}
