package com.lfhardware.notification.domain;

import com.lfhardware.customer.domain.Customer;
import com.lfhardware.provider.domain.ServiceProvider;
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
@Table(name = "tbl_notification")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tbl_notification_seq")
    @SequenceGenerator(name = "tbl_notification_seq", sequenceName = "tbl_notification_id_seq")
    private Long id;

    private String message;

//    @ManyToOne
//    @JoinColumn(name = "service_provider_id")
//    private ServiceProvider serviceProvider;
//
//    @ManyToOne
//    @JoinColumn(name = "customer_id")
//    private Customer customer;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "is_read")
    private boolean isRead;
}
