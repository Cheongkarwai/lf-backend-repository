package com.lfhardware.shipment.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResult {

    @JsonProperty("summary")
    private OrderSummaryDTO orderSummaryDTO;

    @JsonProperty("success")
    private List<OrderSuccessDTO> orderSuccessDTOs;

    @JsonProperty("fail")
    private List<OrderFailDTO> orderFailDTOs;
}
