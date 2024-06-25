package com.lfhardware.transaction.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionDTO(String id, @JsonProperty("charge_amount") BigDecimal chargeAmount, @JsonProperty("created_at") LocalDateTime createdAt,
                             String currency, @JsonProperty("payment_method") String paymentMethod, String status, @JsonProperty("order_id") Long orderId) {
}
