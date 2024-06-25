package com.lfhardware.customer.cache;

import com.lfhardware.shared.PageInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerAppointmentCacheKey {

    private PageInfo pageRequest;

    private String customerId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerAppointmentCacheKey that = (CustomerAppointmentCacheKey) o;
        return Objects.equals(pageRequest, that.pageRequest) && Objects.equals(customerId, that.customerId) && Objects.equals(bookingDateTime, that.bookingDateTime) && Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pageRequest, customerId, bookingDateTime, status);
    }

    private LocalDateTime bookingDateTime;

    private List<String> status;


}
