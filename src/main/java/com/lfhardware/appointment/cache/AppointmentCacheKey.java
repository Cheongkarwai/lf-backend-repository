package com.lfhardware.appointment.cache;

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
public class AppointmentCacheKey {

    private PageRequest pageRequest;

    private List<String> status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppointmentCacheKey that = (AppointmentCacheKey) o;
        return Objects.equals(pageRequest, that.pageRequest) && Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pageRequest, status);
    }
}
