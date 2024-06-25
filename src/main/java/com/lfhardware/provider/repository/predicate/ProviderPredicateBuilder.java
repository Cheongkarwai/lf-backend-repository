package com.lfhardware.provider.repository.predicate;

import com.lfhardware.provider.domain.ServiceDetails_;
import com.lfhardware.provider.domain.ServiceProvider;
import com.lfhardware.provider.domain.ServiceProvider_;
import com.lfhardware.provider.domain.StateCoverage_;
import com.lfhardware.provider_business.domain.Service_;
import com.lfhardware.state.domain.State_;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProviderPredicateBuilder {

    public static jakarta.persistence.criteria.Predicate hasStatus(CriteriaBuilder criteriaBuilder, Root<?> root, List<String> statusList){
        List<Predicate> predicates = new ArrayList<>();
        for(String status : statusList){
            predicates.add(criteriaBuilder.equal(root.get(ServiceProvider_.STATUS),status));
        }
        return criteriaBuilder.or(predicates.toArray(Predicate[]::new));
    }

    public static Predicate hasRating(CriteriaBuilder criteriaBuilder, Root<?> root, Double rating){
        return criteriaBuilder.equal(root.get(ServiceProvider_.RATING), rating);
    }

    public static Predicate hasStates(CriteriaBuilder cb, Root<ServiceProvider> root, List<String> states) {
        return root.get(ServiceProvider_.STATE_COVERAGES).get(StateCoverage_.STATE).get(State_.NAME).in(states);
    }

    public static Predicate hasServiceName(CriteriaBuilder cb, Root<ServiceProvider> root, String serviceName) {
        return cb.equal(root.get(ServiceProvider_.SERVICE_DETAILS).get(ServiceDetails_.SERVICE).get(Service_.NAME), serviceName);
    }

}
