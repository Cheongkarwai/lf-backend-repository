package com.lfhardware.product.mapper;

import com.lfhardware.product.domain.Category;
import com.lfhardware.product.dto.CategoryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoryMapper {

    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "name", source = "name")
    })
    CategoryDTO mapToCategoryDTO(Category category);

    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "name", source = "name")
    })
    Category mapToCategory(CategoryDTO category);
}
