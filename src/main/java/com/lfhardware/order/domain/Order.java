package com.lfhardware.order.domain;
import com.lfhardware.shipment.domain.ShippingDetails;
import com.lfhardware.transaction.domain.Transaction;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_order")
@NamedEntityGraphs({
        @NamedEntityGraph(name="Orders",attributeNodes = {@NamedAttributeNode("shippingDetails"),
        @NamedAttributeNode(value="orderDetails",subgraph = "orderDetails-stock")},
        subgraphs = {
                @NamedSubgraph(name = "orderDetails-stock",attributeNodes = {@NamedAttributeNode("stock")})
        }),
        @NamedEntityGraph(name="OrderDetails",attributeNodes = {@NamedAttributeNode("shippingDetails"),
                @NamedAttributeNode(value="orderDetails",subgraph = "orderDetails-stock")},
                subgraphs = {
                        @NamedSubgraph(name = "orderDetails-stock",attributeNodes = {@NamedAttributeNode("stock")})
                }),
})
@NamedQueries({
        @NamedQuery(name = "Order.findAll", query = "FROM Order o"),
        @NamedQuery(name = "Order.findByIdAndUsername",query = "FROM Order o WHERE o.id = :id AND o.username = :username"),
        @NamedQuery(name = "Order.fetchJoinOrderDetailsFetchJoinShippingDetailsFetchJoinStockFetchJoinProductByUsername", query = "FROM Order o JOIN FETCH o.orderDetails od JOIN FETCH o.shippingDetails sd JOIN FETCH od.stock s JOIN FETCH s.product WHERE o.id = :id AND o.username = :username"),

        //Without username
        @NamedQuery(name = "Order.fetchJoinOrderDetailsFetchJoinShippingDetailsFetchJoinStockFetchJoinProduct", query = "FROM Order o JOIN FETCH o.orderDetails od JOIN FETCH o.shippingDetails sd JOIN FETCH od.stock s JOIN FETCH s.product WHERE o.id = :id"),

})
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @GeneratedValue(generator = "orderIdGenerator")
//    @GenericGenerator(name = "orderIdGenerator", strategy = "com.lfhardware.order.domain.OrderIdGenerator")
    private Long id;

    private BigDecimal subtotal;

    private BigDecimal total;

    @Column(name = "shipping_fees")
    private BigDecimal shippingFees;

    private String username;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "delivery_status")
    private DeliveryStatus deliveryStatus;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "shipping_details_id")
    private ShippingDetails shippingDetails;

    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST)
    private Set<OrderDetails> orderDetails = new HashSet<>();

//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "username")
//    private User user;

    @OneToOne(optional = false, mappedBy = "order", fetch = FetchType.LAZY)
    private Transaction transaction;

    public void addOrderDetails(OrderDetails orderDetails){
        this.orderDetails.add(orderDetails);
        orderDetails.setOrder(this);
    }

    public void setShippingDetails(ShippingDetails shippingDetails){
        this.shippingDetails = shippingDetails;
        shippingDetails.setOrder(this);
    }
}
