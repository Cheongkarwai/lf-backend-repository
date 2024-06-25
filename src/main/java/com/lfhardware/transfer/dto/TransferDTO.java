package com.lfhardware.transfer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TransferDTO {

    private String currency;

    private long amount;

    @JsonProperty("recipient_id")
    private String recipientId;

    @JsonProperty("transfer_group")
    private String transferGroup;
}
