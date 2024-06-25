package com.lfhardware.shipment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RateCheckingBulk extends BulkDTO{

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

}
