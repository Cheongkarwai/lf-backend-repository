package com.lfhardware.report.mapper;

import com.lfhardware.order.domain.Order;
import com.lfhardware.report.dto.MonthlySalesStat;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ReportMapper {

    MonthlySalesStat mapToMonthlySales(Order order);
}
