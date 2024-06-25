package com.lfhardware.order.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public record OrderItemInput(@JsonProperty("stock_id") Long stockId, @JsonProperty("cart_id") Long cartId, int quantity) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItemInput that = (OrderItemInput) o;
        return quantity == that.quantity && Objects.equals(stockId, that.stockId) && Objects.equals(cartId, that.cartId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stockId, cartId, quantity);
    }
}
