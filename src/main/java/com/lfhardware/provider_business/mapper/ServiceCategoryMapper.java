package com.lfhardware.provider_business.mapper;

import com.lfhardware.provider_business.domain.Service;
import com.lfhardware.provider_business.domain.ServiceCategory;
import com.lfhardware.provider_business.dto.ServiceDTO;
import com.lfhardware.provider_business.dto.ServiceGroupByCategoryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ServiceCategoryMapper {

    List<ServiceGroupByCategoryDTO> mapToServiceGroupByCategoryDTOs(List<ServiceCategory> serviceCategories);

    @Mappings({
            @Mapping(target = "id",source = "id"),
            @Mapping(target = "name",source = "name"),
    })
    ServiceGroupByCategoryDTO mapToServiceGroupByCategoryDTO(ServiceCategory serviceCategory);

    @Mappings({
            @Mapping(target = "id",source = "id"),
            @Mapping(target = "name",source = "name"),
    })
    ServiceDTO mapToServiceDTO(Service service);
}
