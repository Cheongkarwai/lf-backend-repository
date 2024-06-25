package com.lfhardware.order.mapper;

import com.lfhardware.address.mapper.AddressMapper;
import com.lfhardware.order.domain.Order;
import com.lfhardware.order.dto.OrderDetailsDTO;
import com.lfhardware.order.dto.OrderProductDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {OrderItemMapper.class, AddressMapper.class})
public interface OrderDetailsMapper {

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "shippingFees", target = "shippingFees"),
            @Mapping(source = "subtotal", target = "subtotal"),
            @Mapping(source = "total", target = "total"),
            @Mapping(source = "shippingDetails.recipient.firstName", target = "shippingDetails.recipient.firstName"),
            @Mapping(source = "shippingDetails.recipient.lastName", target = "shippingDetails.recipient.lastName"),
            @Mapping(source = "shippingDetails.recipient.phoneNumber", target = "shippingDetails.recipient.phoneNumber"),
            @Mapping(source = "shippingDetails.recipient.emailAddress", target = "shippingDetails.recipient.emailAddress"),
            @Mapping(source = "shippingDetails.recipient.address", target = "shippingDetails.recipient.address"),
            @Mapping(source = "shippingDetails.id", target = "shippingDetails.id"),
            @Mapping(source = "orderDetails", target = "items"),
            @Mapping(source = "deliveryStatus",target = "deliveryStatus"),
            @Mapping(source = "paymentStatus", target = "paymentStatus"),
            @Mapping(source = "createdAt", target = "createdAt"),
            @Mapping(source = "modifiedAt", target = "modifiedAt"),
            @Mapping(source = "username", target = "username")
    })
    OrderDetailsDTO mapToOrderDetailsDTO(Order order);

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "shippingFees", target = "shippingFees"),
            @Mapping(source = "subtotal", target = "subtotal"),
            @Mapping(source = "total", target = "total"),
            @Mapping(source = "orderDetails", target = "items"),
            @Mapping(source = "deliveryStatus",target = "deliveryStatus"),
            @Mapping(source = "paymentStatus", target = "paymentStatus"),
            @Mapping(source = "createdAt", target = "createdAt"),
            @Mapping(source = "username", target = "username")
    })
    OrderProductDTO mapToOrderProductDTO(Order order);
}
