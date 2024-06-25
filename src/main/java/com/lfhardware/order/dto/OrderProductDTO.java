package com.lfhardware.order.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lfhardware.order.domain.DeliveryStatus;
import com.lfhardware.order.domain.PaymentStatus;
import com.lfhardware.shipment.dto.ShippingDetailsDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderProductDTO(Long id, @JsonProperty("shipping_fees") BigDecimal shippingFees, BigDecimal subtotal,
                              BigDecimal total, BigDecimal taxAmount, List<OrderItemDTO> items,
                              @JsonProperty("delivery_status") DeliveryStatus deliveryStatus,
                              @JsonProperty("payment_status") PaymentStatus paymentStatus, @JsonProperty("created_at") LocalDateTime createdAt,
                              String username) {
}
