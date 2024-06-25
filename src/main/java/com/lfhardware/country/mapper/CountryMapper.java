package com.lfhardware.country.mapper;

import com.lfhardware.country.dto.CountryDTO;
import com.lfhardware.country.domain.Country;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CountryMapper {

    @Mappings({
            @Mapping(source = "name",target = "name"),
            @Mapping(source = "id",target = "id")
    })
    CountryDTO mapToCountryDTO(Country city);

}
