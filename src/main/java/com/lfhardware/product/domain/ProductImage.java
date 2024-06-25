package com.lfhardware.product.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_product_image")
public class ProductImage {

    @Id
    private String url;

    @Column(length = 50)
    private String type;

    @Column(name = "object_key")
    private String objectKey;

    private String bucket;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
