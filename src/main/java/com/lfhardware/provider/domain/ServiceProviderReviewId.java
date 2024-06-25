package com.lfhardware.provider.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
//@Embeddable
//public class ServiceProviderReviewId implements Serializable {
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        ServiceProviderReviewId that = (ServiceProviderReviewId) o;
//        return Objects.equals(customerId, that.customerId) && Objects.equals(serviceProviderId, that.serviceProviderId);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(customerId, serviceProviderId);
//    }
//
//    @Column(name = "customer_id")
//    private String customerId;
//
//    @Column(name = "service_provider_id")
//    private String serviceProviderId;
//
//
//}
