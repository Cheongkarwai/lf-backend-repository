package com.lfhardware.stock.domain;

import com.lfhardware.order.domain.OrderDetails;
import com.lfhardware.product.domain.Product;
import jakarta.persistence.*;
import jdk.jfr.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_stock")
@NamedQueries({
        @NamedQuery(name = "Stock.findByProductIdAndSize", query = "SELECT s FROM Stock s WHERE s.product.id = :id AND s.size = :size")
})
public class Stock {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private int quantity;

    private String measurement;

    private Double length;

    private Double width;

    private Double height;

    private Double weight;

    @Column(name = "modified_at")
    @UpdateTimestamp
    private LocalDateTime modifiedAt;

    @Enumerated(value = EnumType.STRING)
    private Size size;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToMany(mappedBy = "stock")
    private Set<OrderDetails> orderDetails = new HashSet<>();
}
