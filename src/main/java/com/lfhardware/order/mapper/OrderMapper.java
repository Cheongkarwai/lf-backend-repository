package com.lfhardware.order.mapper;

import com.lfhardware.order.domain.Order;
import com.lfhardware.order.dto.OrderDTO;
import com.lfhardware.order.dto.OrderDetailsDTO;
import com.lfhardware.order.dto.OrderInput;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {

    @Mappings({
            @Mapping(source = "recipient.firstName", target = "shippingDetails.recipient.firstName"),
            @Mapping(source = "recipient.lastName", target = "shippingDetails.recipient.lastName"),
            @Mapping(source = "recipient.emailAddress", target = "shippingDetails.recipient.emailAddress"),
            @Mapping(source = "recipient.phoneNumber", target = "shippingDetails.recipient.phoneNumber"),
            @Mapping(source = "recipient.address.addressLine1", target = "shippingDetails.recipient.address.addressLine1"),
            @Mapping(source = "recipient.address.addressLine2", target = "shippingDetails.recipient.address.addressLine2"),
            @Mapping(source = "recipient.address.city", target = "shippingDetails.recipient.address.city"),
            @Mapping(source = "recipient.address.state", target = "shippingDetails.recipient.address.state"),
            @Mapping(source = "recipient.address.zipcode", target = "shippingDetails.recipient.address.zipcode"),
            @Mapping(source = "deliveryStatus", target = "deliveryStatus"),
            @Mapping(source = "paymentStatus", target = "paymentStatus"),
            @Mapping(source = "shippingFees", target = "shippingFees"),
            @Mapping(source = "subtotal", target = "subtotal"),
            @Mapping(source = "total", target = "total")

    })
    Order mapToOrderEntity(OrderInput orderInput);


    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "shippingFees", source = "shippingFees"),
            @Mapping(target = "subtotal", source = "subtotal"),
            @Mapping(target = "total", source = "total"),
            @Mapping(target = "deliveryStatus", source = "deliveryStatus"),
            @Mapping(target = "paymentStatus", source = "paymentStatus"),
    })
    OrderDTO mapToOrderDTO(Order order);

    @Mappings({
            @Mapping(source = "recipient.firstName", target = "sendName"),
            //@Mapping(source = "recipient.lastName", target = "shippingDetails.recipient.lastName"),
            @Mapping(source = "recipient.emailAddress", target = "sendEmail"),
            @Mapping(source = "recipient.phoneNumber", target = "sendContact"),
            @Mapping(source = "recipient.address.addressLine1", target = "sendAddress1"),
            @Mapping(source = "recipient.address.addressLine2", target = "sendAddress2"),
            @Mapping(source = "recipient.address.city", target = "sendCity"),
            @Mapping(source = "recipient.address.state", target = "sendState"),
            @Mapping(source = "recipient.address.zipcode", target = "sendCode"),
            @Mapping(source = "recipient.address.country", target = "sendCountry"),
            @Mapping(source = "courier.serviceId", target = "serviceId")
    })
    com.lfhardware.shipment.dto.order.OrderBulkDTO mapToShipmentOrderBulkDTO(OrderInput orderInput);
}
