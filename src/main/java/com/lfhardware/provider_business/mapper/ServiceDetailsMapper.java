package com.lfhardware.provider_business.mapper;

import com.lfhardware.provider.domain.ServiceDetails;
import com.lfhardware.provider_business.domain.Service;
import com.lfhardware.provider_business.dto.ServiceDTO;
import com.lfhardware.provider_business.dto.ServiceDetailsDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ServiceDetailsMapper {

    @Mappings({
            @Mapping(target = "id", source = "service.id"),
            @Mapping(target = "name", source = "service.name"),
    })
    ServiceDTO mapToServiceDTO(ServiceDetails serviceDetails);

    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "name", source = "name"),
    })
    ServiceDTO mapToServiceDTO(Service service);

    @Mapping(target = "name", source = "name")
    ServiceDetailsDTO mapToServiceDetailsDTO(Service service);
}
