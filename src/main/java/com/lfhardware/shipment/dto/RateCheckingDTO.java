package com.lfhardware.shipment.dto;

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
public class RateCheckingDTO {

    @JsonProperty("api_status")
    private String apiStatus;

    @JsonProperty("error_code")
    private String errorCode;

    @JsonProperty("error_remark")
    private String errorRemark;

    private List<RateCheckingResultDTO> result;

}
