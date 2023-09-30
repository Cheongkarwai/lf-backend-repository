package com.lfhardware.charges.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @JsonProperty("total_amount")
    private BigDecimal totalAmount;

    private Payer payer;

    private List<Item> items = new ArrayList<>();

}
