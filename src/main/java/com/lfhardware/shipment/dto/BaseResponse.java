package com.lfhardware.shipment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class BaseResponse<T> {

    @JsonProperty("api_status")
    private String apiStatus;

    @JsonProperty("error_code")
    private int errorCode;

    @JsonProperty("error_remark")
    private String errorRemark;

    private List<T> result;
}
