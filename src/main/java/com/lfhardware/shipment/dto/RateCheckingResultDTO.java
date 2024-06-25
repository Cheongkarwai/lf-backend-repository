package com.lfhardware.shipment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RateCheckingResultDTO {

    @JsonProperty("REQ_ID")
    private String reqId;

    private String status;

    private String remarks;

    private List<RateDTO> rates = new ArrayList<>();

    @JsonProperty("pgeon_point")
    private PgeonPointDTO pgeonPoints;
}
