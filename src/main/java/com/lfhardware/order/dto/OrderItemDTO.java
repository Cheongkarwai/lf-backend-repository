package com.lfhardware.order.dto;

import com.lfhardware.stock.domain.Size;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemDTO(UUID id, String name, int quantity, Size size, BigDecimal price) {
}
