package com.lfhardware.form.domain;

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
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class FormId implements Serializable {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FormId formId = (FormId) o;
        return Objects.equals(serviceProviderId, formId.serviceProviderId) && Objects.equals(serviceId, formId.serviceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceProviderId, serviceId);
    }

    @Column(name = "service_provider_id")
    private String serviceProviderId;

    @Column(name = "service_id")
    private Long serviceId;


}
