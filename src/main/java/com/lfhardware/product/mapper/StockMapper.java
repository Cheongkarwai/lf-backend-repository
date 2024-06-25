package com.lfhardware.product.mapper;

import com.lfhardware.product.dto.StockDTO;
import com.lfhardware.product.dto.StockInput;
import com.lfhardware.stock.domain.Stock;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface StockMapper {

    @Mappings({
            @Mapping(target = "size", source = "size"),
            @Mapping(target = "availableQuantity", source = "quantity"),
            @Mapping(target = "length", source = "length"),
            @Mapping(target = "width", source = "width"),
            @Mapping(target = "height", source = "height"),
            @Mapping(target = "weight", source = "weight"),
    })
    StockDTO mapToStock(Stock stock);

    @Mappings({
            @Mapping(target = "size", source = "size"),
            @Mapping(target = "quantity", source = "quantity"),
            @Mapping(target = "length", source = "length"),
            @Mapping(target = "width", source = "width"),
            @Mapping(target = "height", source = "height"),
            @Mapping(target = "weight", source = "weight"),
    })
    Stock mapToStock(StockInput stockInput);

//    default Stock mapToStock(StockInput stockInput){
//        Stock stock = new Stock();
//        stock.set
//        return stock;
//    }
}
