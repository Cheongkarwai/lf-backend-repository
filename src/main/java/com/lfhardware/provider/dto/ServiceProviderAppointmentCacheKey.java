package com.lfhardware.provider.dto;

import com.lfhardware.core.dto.PageRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceProviderAppointmentCacheKey {

    private String serviceProviderId;

    private List<String> status;

    private PageRequest pageRequest;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceProviderAppointmentCacheKey that = (ServiceProviderAppointmentCacheKey) o;
        return Objects.equals(serviceProviderId, that.serviceProviderId) && Objects.equals(status, that.status) && Objects.equals(pageRequest, that.pageRequest);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceProviderId, status, pageRequest);
    }

}
