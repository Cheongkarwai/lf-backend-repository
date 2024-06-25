package com.lfhardware.shipment.dto.payment;

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
public class PaymentResultDTO{

    @JsonProperty("orderno")
    private String orderNo;

    @JsonProperty("messagenow")
    private String messageNow;

    @JsonProperty("parcel")
    private List<Parcel> parcels;
}
