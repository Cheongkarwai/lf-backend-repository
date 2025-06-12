package com.lfhardware.provider.dto;

import com.lfhardware.core.dto.PageRequest;
import com.lfhardware.core.repository.Search;
import com.lfhardware.core.repository.Sort;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceProviderPageRequest {

    private BigDecimal minPrice;

    private BigDecimal maxPrice;

    private Double rating;

    private String serviceName;

    private List<String> states;

    private String status;

    private int pageSize;

    private int page;

    private Sort sort;

    private Search search;


    public ServiceProviderPageRequest(int pageSize, int page, Sort sort, Search search, BigDecimal minPrice, BigDecimal maxPrice,
                                      Double rating, String serviceName, List<String> states, String status) {
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.rating = rating;
        this.serviceName = serviceName;
        this.states = states;
        this.status = status;
    }

    public ServiceProviderPageRequest(int pageSize, int page, Sort sort, Search search) {
        this.pageSize = pageSize;
        this.page = page;
        this.sort = sort;
        this.search = search;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ServiceProviderPageRequest that = (ServiceProviderPageRequest) o;
        return Objects.equals(minPrice, that.minPrice) && Objects.equals(maxPrice, that.maxPrice) && Objects.equals(rating, that.rating) && Objects.equals(serviceName, that.serviceName) && Objects.equals(states, that.states);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), minPrice, maxPrice, rating, serviceName, states);
    }
}
