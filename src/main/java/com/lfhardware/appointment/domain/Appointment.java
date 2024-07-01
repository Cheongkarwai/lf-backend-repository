package com.lfhardware.appointment.domain;

import com.lfhardware.auth.domain.Address;
import com.lfhardware.customer.domain.Customer;
import com.lfhardware.provider.domain.ServiceProvider;
import com.lfhardware.provider_business.domain.Service;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_appointment")
@NamedEntityGraph(name = "Appointment.appointmentImage", attributeNodes = {@NamedAttributeNode("appointmentImages"),
        @NamedAttributeNode("service"), @NamedAttributeNode("serviceProvider"), @NamedAttributeNode("customer")})
public class Appointment {

    @ColumnDefault("random_uuid()")
    private UUID id;

    @EmbeddedId
    private AppointmentId appointmentId = new AppointmentId();

    @MapsId("customerId")
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @MapsId("serviceProviderId")
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "service_provider_id")
    private ServiceProvider serviceProvider;

    @MapsId("serviceId")
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "service_id")
    private Service service;

    @Column(name = "booking_datetime")
    @CreationTimestamp
    private LocalDateTime bookingDatetime;

    @Column(name = "estimated_price")
    private BigDecimal estimatedPrice;

    @Column(name = "is_paid")
    private boolean isPaid;

    @Enumerated(value = EnumType.STRING)
    private AppointmentStatus status;

    @Column(name = "completion_datetime")
    private LocalDateTime completionDateTime;

    @UpdateTimestamp
    @Column(name = "status_last_update")
    private LocalDateTime statusLastUpdate;

    @Column(name = "confirmation_datetime")
    private LocalDateTime confirmationDatetime;

    @Column(name = "job_started_datetime")
    private LocalDateTime jobStartedDatetime;

    @Column(name = "job_completion_datetime")
    private LocalDateTime jobCompletionDatetime;

    @Column(name = "review_datetime")
    private LocalDateTime reviewDatetime;

    @Column(name = "checkout_session_id")
    private String checkoutSessionId;

    @Column(name = "payment_intent_id")
    private String paymentIntentId;

    @Column(name = "checkout_url")
    private String checkoutUrl;

    @Column(name = "transfer_group")
    private String transferGroup;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "addressLine1", column = @Column(name = "address_line_1")),
            @AttributeOverride(name = "addressLine2", column = @Column(name = "address_line_2")),
    })
    private Address address;

    @Column(name = "has_review")
    private boolean hasReview;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, mappedBy = "appointment")
    private List<AppointmentImage> appointmentImages = new ArrayList<>();


}
