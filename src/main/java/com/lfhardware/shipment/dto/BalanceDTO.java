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
public class BalanceDTO {

    private String currency;

    private BigDecimal result;

    private WalletDTO wallet;

    @JsonProperty("api_status")
    private String apiStatus;

    @JsonProperty("error_code")
    private String errorCode;

    @JsonProperty("error_remark")
    private String errorRemark;
}
