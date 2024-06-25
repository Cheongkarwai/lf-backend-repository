package com.lfhardware.provider.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class BankDetailsId implements Serializable {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BankDetailsId that = (BankDetailsId) o;
        return Objects.equals(serviceProviderId, that.serviceProviderId) && Objects.equals(bankId, that.bankId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceProviderId, bankId);
    }

    public String getServiceProviderId() {
        return serviceProviderId;
    }

    public void setServiceProviderId(String serviceProviderId) {
        this.serviceProviderId = serviceProviderId;
    }

    public Long getBankId() {
        return bankId;
    }

    public void setBankId(Long bankId) {
        this.bankId = bankId;
    }

    @Column(name = "service_provider_id")
    private String serviceProviderId;

    @Column(name = "bank_id")
    private Long bankId;


}
