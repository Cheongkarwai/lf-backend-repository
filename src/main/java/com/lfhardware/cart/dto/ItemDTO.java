package com.lfhardware.cart.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.lfhardware.product.dto.BrandDTO;
import com.lfhardware.product.dto.CategoryDTO;
import com.lfhardware.stock.domain.Size;

import java.math.BigDecimal;

public record ItemDTO (@JsonProperty("cart_id") String cartId, @JsonProperty("stock_id") Long stockId,@JsonProperty("product_id") Long productId, String name, String description, BigDecimal price, int quantity, @JsonProperty("available_quantity") int availableQuantity,
                       @JsonProperty("size") Size size, double width, double length, double weight, double height){
}
