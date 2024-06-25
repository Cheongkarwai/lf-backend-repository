package com.lfhardware.provider.mapper;

import com.lfhardware.provider.domain.Album;
import com.lfhardware.provider.dto.AlbumDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AlbumMapper {

    @Mappings({
            @Mapping(source = "name",target = "name"),
            @Mapping(source = "description",target = "description")
    })
    Album mapToAlbumEntity(AlbumDTO albumDTO);
}
