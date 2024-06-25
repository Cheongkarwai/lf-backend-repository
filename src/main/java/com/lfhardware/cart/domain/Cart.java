package com.lfhardware.cart.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_cart")
@NamedQueries({
        @NamedQuery(name = "Cart.findByUsername", query = "SELECT u FROM Cart u WHERE u.username = :username")
})
@NamedEntityGraphs({
        @NamedEntityGraph(
                name = "CartItem",
                attributeNodes = {
                @NamedAttributeNode(value="cartDetails",subgraph = "cartDetails-stock")
        },
                subgraphs = {
                @NamedSubgraph(name = "cartDetails-stock", attributeNodes = {@NamedAttributeNode(value="stock",subgraph = "stock-product")}),
                @NamedSubgraph(name = "stock-product",attributeNodes = {@NamedAttributeNode("product")})
        })
})
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @OneToMany(mappedBy = "cart")
    public Set<CartDetails> cartDetails = new HashSet<>();
}
