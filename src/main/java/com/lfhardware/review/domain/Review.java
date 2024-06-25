package com.lfhardware.review.domain;

import com.lfhardware.product.domain.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_review")
public class Review {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    private String description;

    @Column(precision = 3)
    private double rating;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
