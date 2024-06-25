package com.lfhardware.provider.cache;

import com.lfhardware.provider.dto.ServiceProviderPageRequest;
import com.lfhardware.shared.PageInfo;
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
public class ServiceProviderCacheKey {


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceProviderCacheKey that = (ServiceProviderCacheKey) o;
        return Objects.equals(rating, that.rating) && Objects.equals(pageRequest, that.pageRequest) && Objects.equals(status, that.status) && Objects.equals(states, that.states)  && Objects.equals(serviceName, that.serviceName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pageRequest, status, states, rating, serviceName);
    }

    private PageInfo pageRequest;

    private List<String> status;

    private List<String> states;

    private Double rating;

    private String serviceName;

}
