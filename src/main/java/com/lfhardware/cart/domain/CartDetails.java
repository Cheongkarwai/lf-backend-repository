package com.lfhardware.cart.domain;

import com.lfhardware.product.domain.Product;
import com.lfhardware.stock.domain.Stock;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_cart_details")
public class CartDetails {

    private int quantity;

    @EmbeddedId
    private CartDetailsId cartDetailsId = new CartDetailsId();

    @ManyToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "stock_id")
    @MapsId("stockId")
    private Stock stock;

    @ManyToOne(cascade = {CascadeType.MERGE})
    @JoinColumn(name = "cart_id")
    @MapsId("cartId")
    private Cart cart;
}
