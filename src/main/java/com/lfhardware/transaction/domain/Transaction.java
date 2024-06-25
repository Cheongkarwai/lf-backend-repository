package com.lfhardware.transaction.domain;

import com.lfhardware.order.domain.Order;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_transaction")
public class Transaction {

    @Id
    private String id;

    @Column(name = "charge_amount", scale = 10, precision = 2)
    private BigDecimal chargeAmount;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    private String currency;

    @Column(name = "payment_method")
    private String paymentMethod;

    private String status;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;
}
