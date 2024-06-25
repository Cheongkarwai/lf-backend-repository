package com.lfhardware.order.mapper;

import com.lfhardware.order.domain.OrderDetails;
import com.lfhardware.order.dto.OrderItemDTO;
import com.lfhardware.order.dto.OrderItemInput;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderItemMapper {

    @Mappings({
            @Mapping(target = "id", source = "stock.product.id"),
            @Mapping(target = "name", source = "stock.product.name"),
            @Mapping(target = "quantity",source = "quantity"),
            @Mapping(target = "price", source = "stock.product.price"),
            @Mapping(target = "size", source = "stock.size")
    })
    OrderItemDTO mapToOrderItemDTO(OrderDetails orderDetails);
}
