package com.lfhardware.order.dto;

import com.lfhardware.stock.domain.Size;

import java.math.BigDecimal;

public record OrderItemDTO(Long id, String name, int quantity, Size size, BigDecimal price) {
}
