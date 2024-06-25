package com.lfhardware.appointment.domain;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentId implements Serializable {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppointmentId that = (AppointmentId) o;
        return Objects.equals(serviceProviderId, that.serviceProviderId) && Objects.equals(createdAt, that.createdAt) && Objects.equals(serviceId, that.serviceId) && Objects.equals(customerId, that.customerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceProviderId, createdAt, serviceId, customerId);
    }

    @Column(name = "service_id")
    private Long serviceId;

    @Column(name = "service_provider_id")
    private String serviceProviderId;

    @Column(name = "customer_id")
    private String customerId;

    @CreationTimestamp
    @Column(name = "created_at", columnDefinition = "timestamp with time zone")
    private LocalDateTime createdAt;


}
