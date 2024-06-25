package com.lfhardware.provider.dto;

import com.lfhardware.shared.PageInfo;
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

    private PageInfo pageInfo;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceProviderAppointmentCacheKey that = (ServiceProviderAppointmentCacheKey) o;
        return Objects.equals(serviceProviderId, that.serviceProviderId) && Objects.equals(status, that.status) && Objects.equals(pageInfo, that.pageInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceProviderId, status, pageInfo);
    }

}
