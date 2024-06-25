package com.lfhardware.product.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lfhardware.stock.domain.Size;

import java.util.List;

public record StockDTO(Size size, @JsonProperty("available_quantity") int availableQuantity,
                       Double length, Double height, Double width, Double weight) {
}
