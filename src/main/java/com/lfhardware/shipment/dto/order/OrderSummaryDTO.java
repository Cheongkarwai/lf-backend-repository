package com.lfhardware.shipment.dto.order;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderSummaryDTO {

    @JsonProperty("courier_service")
    private List<String> courierServices;

    @JsonProperty("orders_number")
    private List<String> ordersNumber;

    @JsonProperty("total_paid_amount")
    private BigDecimal totalPaidAmount;

    @JsonProperty("total_success")
    private int totalSuccess;

    @JsonProperty("total_fail")
    private int totalFail;
}
