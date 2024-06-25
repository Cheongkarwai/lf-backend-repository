package com.lfhardware.provider.domain;

import com.lfhardware.provider_business.domain.Service;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_service_details", uniqueConstraints ={
        @UniqueConstraint(columnNames={"service_provider_id", "service_id"})
})
public class ServiceDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceDetails that = (ServiceDetails) o;
        return Objects.equals(service, that.service) && Objects.equals(serviceProvider, that.serviceProvider);
    }

    @Override
    public int hashCode() {
        return Objects.hash(service, serviceProvider);
    }

    @ManyToOne
    @JoinColumn(name="service_id")
    private Service service;

    @ManyToOne
    @JoinColumn(name="service_provider_id")
    private ServiceProvider serviceProvider;
}
