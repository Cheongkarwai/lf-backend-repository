package com.lfhardware.cart.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table
public class CartItem {

    @Id
    @GeneratedValue
    private Long id;

    public int quantity;

    public Long cartId;

    public Long productId;
}
