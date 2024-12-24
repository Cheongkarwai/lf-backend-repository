package com.lfhardware.provider.cache;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceProviderReviewCacheKey extends PageableCacheKey{

    private String serviceProviderId;

    private Double rating;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServiceProviderReviewCacheKey cacheKey)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(serviceProviderId, cacheKey.serviceProviderId) && Objects.equals(rating, cacheKey.rating);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), serviceProviderId, rating);
    }
}
