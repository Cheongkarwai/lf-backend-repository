package com.lfhardware.order.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lfhardware.cart.dto.CartItemDTO;
import com.lfhardware.order.domain.DeliveryStatus;
import com.lfhardware.order.domain.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderDTO(Long id, @JsonProperty("shipping_fees") BigDecimal shippingFees, BigDecimal subtotal, BigDecimal total,
                       @JsonProperty("delivery_status") DeliveryStatus deliveryStatus, @JsonProperty("payment_status") PaymentStatus paymentStatus,
                       @JsonProperty("created_at") LocalDateTime createdAt) {

}

