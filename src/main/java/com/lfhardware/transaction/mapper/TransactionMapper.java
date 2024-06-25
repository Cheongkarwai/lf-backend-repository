package com.lfhardware.transaction.mapper;

import com.lfhardware.transaction.domain.Transaction;
import com.lfhardware.transaction.dto.TransactionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TransactionMapper {

    @Mappings({
            @Mapping(target = "id",source = "id"),
            @Mapping(target = "chargeAmount", source = "chargeAmount"),
            @Mapping(target = "createdAt", source = "createdAt"),
            @Mapping(target = "currency", source = "currency"),
            @Mapping(target = "paymentMethod", source = "paymentMethod"),
            @Mapping(target = "status", source = "status"),
            @Mapping(target = "orderId", source = "order.id")
    })
    TransactionDTO mapToTransactionDTO(Transaction transaction);
}
