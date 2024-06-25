package com.lfhardware.provider.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class StateCoverageId implements Serializable {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StateCoverageId that = (StateCoverageId) o;
        return Objects.equals(serviceProviderId, that.serviceProviderId) && Objects.equals(stateId, that.stateId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceProviderId, stateId);
    }

    @Column(name = "service_provider_id")
    private String serviceProviderId;

    @Column(name = "state_id")
    private Long stateId;
}
