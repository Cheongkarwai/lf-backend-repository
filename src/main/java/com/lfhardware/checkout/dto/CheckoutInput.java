package com.lfhardware.checkout.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lfhardware.customer.dto.CustomerInfoInput;
import com.stripe.model.LineItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutInput {

    private List<ServiceItemInput> items = new ArrayList<>();

    @JsonProperty("service_provider_id")
    private String serviceProviderId;

    @JsonProperty("processing_fees")
    private BigDecimal processingFees;
}
