package com.lfhardware.provider.domain;

import com.lfhardware.customer.domain.Customer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_service_provider_review")
public class ServiceProviderReview {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "service_provider_id_seq_generator")
    @SequenceGenerator(name = "service_provider_id_seq_generator", sequenceName = "tbl_service_provider_review_id_seq", allocationSize = 1)
    private Long id;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    private String description;

    private double rating;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "service_provider_id")
//    @MapsId("serviceProviderId")
    private ServiceProvider serviceProvider;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id")
//    @MapsId("customerId")
    private Customer customer;

}
