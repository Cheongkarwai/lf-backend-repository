package com.lfhardware.shipment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BulkInput{

    @JsonProperty("pick_code")
    private String pickCode;

    @JsonProperty("pick_state")
    private String pickState;

    @JsonProperty("pick_country")
    private String pickCountry;

    @JsonProperty("send_code")
    private String sendCode;

    @JsonProperty("send_country")
    private String sendCountry;

    private Double weight;

    private Double width;

    private Double length;

    private Double height;

    @JsonProperty("date_coll")
    private LocalDate dateCollect;
}
