package com.lfhardware.address.mapper;

import com.lfhardware.auth.domain.Address;
import com.lfhardware.auth.dto.AddressDTO;
import com.lfhardware.customer.dto.CustomerAddressInput;
import com.lfhardware.provider.dto.BusinessAddressInput;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingInheritanceStrategy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, mappingInheritanceStrategy = MappingInheritanceStrategy.AUTO_INHERIT_FROM_CONFIG)
public interface AddressMapper {

    @Mapping(target = "addressLine1", source = "addressLine1")
    @Mapping(target = "addressLine2", source = "addressLine2")
    @Mapping(target = "city", source = "city")
    @Mapping(target = "state", source = "state")
    @Mapping(target = "zipcode", source = "zipcode")
    Address mapToAddress(BusinessAddressInput addressInput);

    @Mapping(target = "addressLine1", source = "addressLine1")
    @Mapping(target = "addressLine2", source = "addressLine2")
    @Mapping(target = "city", source = "city")
    @Mapping(target = "state", source = "state")
    @Mapping(target = "zipcode", source = "zipcode")
    Address mapToAddress(CustomerAddressInput customerAddressInput);

    @Mapping(target = "addressLine1", source = "addressLine1")
    @Mapping(target = "addressLine2", source = "addressLine2")
    @Mapping(target = "city", source = "city")
    @Mapping(target = "state", source = "state")
    @Mapping(target = "zipcode", source = "zipcode")
    Address mapToAddress(AddressDTO addressDTO);

    @Mapping(target = "addressLine1", source = "addressLine1")
    @Mapping(target = "addressLine2", source = "addressLine2")
    @Mapping(target = "city", source = "city")
    @Mapping(target = "state", source = "state")
    @Mapping(target = "zipcode", source = "zipcode")
    AddressDTO mapToAddressDTO(Address address);
}
