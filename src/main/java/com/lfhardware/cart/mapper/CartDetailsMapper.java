package com.lfhardware.cart.mapper;

import com.lfhardware.cart.domain.CartDetails;
import com.lfhardware.cart.dto.ItemDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CartDetailsMapper {


    @Mappings({
            @Mapping(target = "cartId", source = "cart.id"),
            @Mapping(target = "stockId", source = "stock.id"),
            @Mapping(target = "productId", source = "stock.product.id"),
            @Mapping(target = "name", source = "stock.product.name"),
            @Mapping(target = "price", source = "stock.product.price"),
            @Mapping(target = "description", source = "stock.product.description"),
            @Mapping(target = "quantity", source = "quantity"),
            @Mapping(target = "availableQuantity", source = "stock.quantity"),
            @Mapping(target = "size", source = "stock.size"),
            @Mapping(target = "width", source = "stock.width"),
            @Mapping(target = "height", source = "stock.height"),
            @Mapping(target = "weight", source = "stock.weight"),
            @Mapping(target = "length", source = "stock.length")
    })
    ItemDTO mapToItemDTO(CartDetails cartDetails);
}
