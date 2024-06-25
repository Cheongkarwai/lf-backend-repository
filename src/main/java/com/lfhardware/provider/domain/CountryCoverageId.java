package com.lfhardware.provider.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class CountryCoverageId implements Serializable {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CountryCoverageId that = (CountryCoverageId) o;
        return Objects.equals(serviceProviderId, that.serviceProviderId) && Objects.equals(countryId, that.countryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceProviderId, countryId);
    }

    @Column(name = "service_provider_id")
    private String serviceProviderId;

    @Column(name = "country_id")
    private Long countryId;
}
