package com.lfhardware.product.mapper;

import com.lfhardware.product.domain.Brand;
import com.lfhardware.product.dto.BrandDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BrandMapper {

    @Mappings({
            @Mapping(target = "id",source = "id"),
            @Mapping(target = "name",source = "name")
    })
    BrandDTO mapToBrandDTO(Brand brand);

    @Mappings({
            @Mapping(target = "id",source = "id"),
            @Mapping(target = "name",source = "name")
    })
    Brand mapToBrand(BrandDTO brand);
}
