package com.lfhardware.appointment.cache;

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
public class AppointmentCacheKey {

    private PageInfo pageInfo;

    private List<String> status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppointmentCacheKey that = (AppointmentCacheKey) o;
        return Objects.equals(pageInfo, that.pageInfo) && Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pageInfo, status);
    }
}
