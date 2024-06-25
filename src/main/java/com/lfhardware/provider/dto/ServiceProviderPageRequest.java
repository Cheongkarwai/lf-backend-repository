package com.lfhardware.provider.dto;

import com.lfhardware.shared.PageInfo;
import com.lfhardware.shared.Search;
import com.lfhardware.shared.Sort;
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
public class ServiceProviderPageRequest extends PageInfo {

    private BigDecimal minPrice;

    private BigDecimal maxPrice;

    private Double rating;

    private String serviceName;

    private List<String> states;

    private String status;


    public ServiceProviderPageRequest(int pageSize, int page, Sort sort, Search search, BigDecimal minPrice, BigDecimal maxPrice,
                                      Double rating, String serviceName, List<String> states, String status) {
        super(pageSize, page, sort, search);
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.rating = rating;
        this.serviceName = serviceName;
        this.states = states;
        this.status = status;
    }

    public ServiceProviderPageRequest(int pageSize, int page, Sort sort, Search search) {
        super(pageSize, page, sort, search);
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
