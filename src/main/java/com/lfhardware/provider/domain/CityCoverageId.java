package com.lfhardware.provider.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class CityCoverageId implements Serializable {

    @Column(name = "service_provider_id")
    private String serviceProviderId;

    @Column(name = "city_id")
    private Long cityId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CityCoverageId that = (CityCoverageId) o;
        return Objects.equals(serviceProviderId, that.serviceProviderId) && Objects.equals(cityId, that.cityId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceProviderId, cityId);
    }
}
