package com.lfhardware.customer.domain;

import com.lfhardware.appointment.domain.Appointment;
import com.lfhardware.appointment.domain.AppointmentId;
import com.lfhardware.auth.domain.Address;
import com.lfhardware.provider.domain.ServiceProviderReview;
import com.lfhardware.shared.CommonConstant;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_customer")
public class Customer {

    @Id
    private String id;

    private String name;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "addressLine1", column = @Column(name = "address_line_1")),
            @AttributeOverride(name = "addressLine2", column = @Column(name = "address_line_2")),
    })
    private Address address;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "phone_number_prefix")
    private String phoneNumberPrefix;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, mappedBy = "customer")
    private List<ServiceProviderReview> reviews = new ArrayList<>();


}
