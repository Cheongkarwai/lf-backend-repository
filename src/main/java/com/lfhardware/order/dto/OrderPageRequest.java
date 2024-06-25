package com.lfhardware.order.dto;

import com.lfhardware.order.domain.DeliveryStatus;
import com.lfhardware.shared.PageInfo;
import com.lfhardware.shared.Search;
import com.lfhardware.shared.Sort;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderPageRequest extends PageInfo {

    private DeliveryStatus deliveryStatus;

    public OrderPageRequest(int pageSize, int page, Sort sort, Search search, DeliveryStatus deliveryStatus){
        super(pageSize, page, sort, search);
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
