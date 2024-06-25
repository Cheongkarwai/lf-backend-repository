package com.lfhardware.provider.cache;

import com.lfhardware.shared.PageInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageableCacheKey {

    private PageInfo pageInfo;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PageableCacheKey that = (PageableCacheKey) o;
        return Objects.equals(pageInfo, that.pageInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pageInfo);
    }
}
