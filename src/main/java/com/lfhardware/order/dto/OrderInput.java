package com.lfhardware.order.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lfhardware.order.domain.DeliveryStatus;
import com.lfhardware.order.domain.PaymentStatus;

import java.math.BigDecimal;
import java.util.List;

public record OrderInput(RecipientDTO recipient, String paymentMethod, String shippingMethod,
                         BigDecimal subtotal, BigDecimal total, @JsonProperty("shipping_fees") BigDecimal shippingFees,
                         List<OrderItemInput> items,
                         @JsonProperty("delivery_status") DeliveryStatus deliveryStatus, @JsonProperty("payment_status") PaymentStatus paymentStatus,
                         CourierInput courier
) {
}
