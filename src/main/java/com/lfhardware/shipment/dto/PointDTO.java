package com.lfhardware.shipment.dto;

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
public class PointDTO {

    @JsonProperty("point_id")
    protected String id;

    @JsonProperty("point_name")
    protected String name;

    @JsonProperty("point_contact")
    protected String contact;

    @JsonProperty("point_lat")
    protected Double lat;

    @JsonProperty("point_lon")
    protected Double lon;

    @JsonProperty("point_addr1")
    protected String addr1;

    @JsonProperty("point_addr2")
    protected String addr2;

    @JsonProperty("point_addr3")
    protected String addr3;

    @JsonProperty("point_addr4")
    protected String addr4;

    @JsonProperty("point_city")
    protected String city;

    @JsonProperty("point_state")
    protected String state;

    @JsonProperty("point_postcode")
    protected String postcode;

    @JsonProperty("price")
    protected BigDecimal price;
}
