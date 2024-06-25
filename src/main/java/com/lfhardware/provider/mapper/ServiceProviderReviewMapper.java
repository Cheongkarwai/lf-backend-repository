package com.lfhardware.provider.mapper;

import com.lfhardware.provider.domain.ServiceProviderReview;
import com.lfhardware.provider.dto.ServiceProviderDTO;
import com.lfhardware.provider.dto.ServiceProviderReviewDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ServiceProviderReviewMapper {

    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "rating", source = "rating")
    @Mapping(target = "serviceProvider", source = "serviceProvider")
    @Mapping(target = "customer", source = "customer")
    ServiceProviderReviewDTO mapToServiceProviderReviewDTO(ServiceProviderReview serviceProviderReview);
}
