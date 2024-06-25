package com.lfhardware.state.mapper;

import com.lfhardware.state.domain.State;
import com.lfhardware.state.dto.StateDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface StateMapper {

    @Mappings({
            @Mapping(source = "name",target = "name"),
            @Mapping(source = "id",target = "id")
    })
    StateDTO mapToStateDTO(State state);

}
