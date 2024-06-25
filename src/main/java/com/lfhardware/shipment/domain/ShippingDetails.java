package com.lfhardware.shipment.domain;

import com.lfhardware.order.domain.Order;
import com.lfhardware.order.domain.Recipient;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_shipping_details")
@Entity
public class ShippingDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "shippingDetails")
    private Order order;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "firstName", column = @Column(name = "first_name")),
            @AttributeOverride(name = "lastName", column = @Column(name = "last_name")),
            @AttributeOverride(name = "phoneNumber", column = @Column(name = "phone_number")),
            @AttributeOverride(name = "emailAddress", column = @Column(name = "email_address"))
    })
    private Recipient recipient;


    @Column(name = "out_for_delivery_at")
    private LocalDateTime outForDeliveryAt;

    @Column(name = "delivered_at")
    private LocalDateTime deliveredAt;
}
