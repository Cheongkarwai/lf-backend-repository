package com.lfhardware.cart.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.lfhardware.stock.domain.Size;

public record CartItemDTO(@JsonProperty("product_id") Long productId, int quantity, @JsonProperty("size") Size size){}
