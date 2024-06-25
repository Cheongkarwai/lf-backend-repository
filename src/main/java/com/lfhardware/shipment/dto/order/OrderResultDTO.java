package com.lfhardware.shipment.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResultDTO {

    @JsonProperty("api_status")
    private String apiStatus;

    @JsonProperty("error_code")
    private String errorCode;

    @JsonProperty("error_remark")
    private String errorRemarks;

    @JsonProperty("result")
    private OrderResult orderResult;
}
