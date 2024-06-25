package com.lfhardware.cart.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO {

   private  BigDecimal subtotal;

   private BigDecimal total;

   @JsonProperty("shipping_fees")
   private BigDecimal shippingFees;

   @JsonProperty("tax_amount")
   private BigDecimal taxAmount;

   private Set<ItemDTO> items = new HashSet<>();

   private String username;
}
