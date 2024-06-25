package com.lfhardware.shipment.dto.order;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderSuccessDTO {

    private String reference;

    private String status;

    private String remarks;

    @JsonProperty("order_number")
    private String orderNumber;

    private String courier;

    @JsonProperty("courier_short")
    private String courierShort;

    private BigDecimal price;

    private BigDecimal tax;

    @JsonProperty("addon_price")
    private BigDecimal addonPrice;

    @JsonProperty("parcel_number")
    private String parcelNumber;

    private String awb;

    @JsonProperty("awb_id_link")
    private String awbIdLink;

    @JsonProperty("tracking_url")
    private String trackingUrl;
}
