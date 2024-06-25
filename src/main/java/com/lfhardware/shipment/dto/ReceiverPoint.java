package com.lfhardware.shipment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class ReceiverPoint {

    @JsonProperty("point_id")
    private String id;

    private String company;

    @JsonProperty("point_name")
    private String name;

    @JsonProperty("point_contact")
    private String contact;

    @JsonProperty("point_lat")
    private Double lat;

    @JsonProperty("point_lon")
    private Double lon;

    @JsonProperty("point_addr1")
    private String addr1;

    @JsonProperty("point_addr2")
    private String addr2;

    @JsonProperty("point_addr3")
    private String addr3;

    @JsonProperty("point_addr4")
    private String addr4;

    @JsonProperty("point_city")
    private String city;

    @JsonProperty("point_state")
    private String state;

    @JsonProperty("point_postcode")
    private String postcode;

    private BigDecimal price;
}
