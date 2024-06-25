package com.lfhardware.product.domain;


import com.lfhardware.review.domain.Review;
import com.lfhardware.stock.domain.Stock;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_product")
@NamedEntityGraphs({
        @NamedEntityGraph(name = "ProductDetails",attributeNodes = {
                @NamedAttributeNode("category"),
                @NamedAttributeNode("brand"),
                @NamedAttributeNode("stocks"),
                @NamedAttributeNode("productImages"),
                @NamedAttributeNode("reviews")
        })
})
@NamedQueries({
        @NamedQuery(name="ProductDetails.findById",query = "FROM Product u WHERE u.id = :id"),
        @NamedQuery(name= "Product.findByName", query = "FROM Product u WHERE u.name = :name")
//        @NamedQuery(name = "ProductLeftJoinStock", query = "FROM Product p LEFT JOIN FETCH Stock s ON p.stocks = s.product")
})
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private BigDecimal price;

    private String description;

    @Column(name = "modified_at")
    @UpdateTimestamp
    private LocalDateTime modifiedAt;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "product", cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    private Set<Stock> stocks = new HashSet<>();

    @OneToMany(mappedBy = "product", cascade = {CascadeType.PERSIST})
    private Set<ProductImage> productImages = new HashSet<>();

    @OneToMany(mappedBy = "product", cascade = {CascadeType.PERSIST})
    private Set<Review> reviews = new HashSet<>();

    public void setStocks(Set<Stock> stocks){
        this.stocks.addAll(stocks);
        stocks.forEach(e-> e.setProduct(this));
    }

    public void setProductImages(Set<ProductImage> productImages){
        this.productImages.addAll(productImages);
        productImages.forEach(e-> e.setProduct(this));
    }

    public void addReview(Review review) {
        review.setProduct(this);
        this.reviews.add(review);
    }
}
