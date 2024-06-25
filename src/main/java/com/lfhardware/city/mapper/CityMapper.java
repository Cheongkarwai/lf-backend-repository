package com.lfhardware.city.mapper;

import com.lfhardware.city.dto.CityDTO;
import com.lfhardware.city.domain.City;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CityMapper {

    @Mappings({
            @Mapping(source = "name",target = "name"),
            @Mapping(source = "id",target = "id")
    })
    CityDTO mapToCityDTO(City city);

}
