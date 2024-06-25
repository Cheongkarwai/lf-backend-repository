package com.lfhardware.appointment.repository.predicate;

import com.lfhardware.appointment.domain.Appointment_;
import com.lfhardware.customer.domain.Customer_;
import com.lfhardware.provider.domain.ServiceProvider_;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AppointmentPredicateBuilder {

    public static Predicate hasCustomerId(CriteriaBuilder criteriaBuilder, Root<?> root, String customerId){
        return criteriaBuilder.equal(root.get(Appointment_.CUSTOMER).get(Customer_.ID), customerId);
    }

    public static Predicate hasStatus(CriteriaBuilder criteriaBuilder, Root<?> root, List<String> statusList){
        List<Predicate> predicates = new ArrayList<>();
        for(String status : statusList){
            predicates.add(criteriaBuilder.equal(root.get(Appointment_.STATUS),status));
        }
        return criteriaBuilder.or(predicates.toArray(Predicate[]::new));
    }

    public static Predicate hasServiceId(CriteriaBuilder criteriaBuilder, Root<?> root, String serviceProviderId) {
        return criteriaBuilder.equal(root.get(Appointment_.SERVICE_PROVIDER).get(ServiceProvider_.ID), serviceProviderId);
    }

    public static Predicate hasBookingDateTime(CriteriaBuilder criteriaBuilder, Root<?> root, LocalDateTime bookingDatetime) {
        return criteriaBuilder.equal((root.get(Appointment_.BOOKING_DATETIME)).as(LocalDate.class), bookingDatetime.toLocalDate());
    }

}
