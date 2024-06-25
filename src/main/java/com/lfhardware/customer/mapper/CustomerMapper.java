package com.lfhardware.customer.mapper;

import com.lfhardware.address.mapper.AddressMapper;
import com.lfhardware.customer.dto.CustomerDTO;
import com.lfhardware.customer.domain.Customer;
import com.lfhardware.customer.dto.CustomerInfoInput;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {
        AddressMapper.class
})
public interface CustomerMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "address", source = "address")
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    CustomerDTO mapToCustomerDTO(Customer customer);

    @Mapping(target = "address", source = "address")
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    @Mapping(target = "phoneNumberPrefix", source = "phoneNumberPrefix")
    Customer mapToCustomer(CustomerInfoInput customerInfoInput);
}
