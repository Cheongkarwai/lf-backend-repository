package com.lfhardware.shipment.dto.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Parcel {

    @JsonProperty("parcelno")
    private String parcelNo;

    private String awb;

    @JsonProperty("awb_id_link")
    private String awbIdLink;

    @JsonProperty("tracking_url")
    private String trackingUrl;


}
