package com.lfhardware.order.dto;

import com.lfhardware.order.domain.DeliveryStatus;
import com.lfhardware.core.dto.PageRequest;
import com.lfhardware.core.repository.Search;
import com.lfhardware.core.repository.Sort;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class OrderPageRequest {

    private DeliveryStatus deliveryStatus;

    private int pageSize;

    private int page;

    private Sort sort;

    private Search search;

    public OrderPageRequest(int pageSize, int page, Sort sort, Search search, DeliveryStatus deliveryStatus) {
        this.pageSize = pageSize;
        this.page = page;
        this.sort = sort;
        this.search = search;
        this.deliveryStatus = deliveryStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        OrderPageRequest that = (OrderPageRequest) o;
        return deliveryStatus == that.deliveryStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), deliveryStatus);
    }
}
