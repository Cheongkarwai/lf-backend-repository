package com.lfhardware.appointment.mapper;

import com.lfhardware.address.mapper.AddressMapper;
import com.lfhardware.appointment.dto.AppointmentDTO;
import com.lfhardware.appointment.domain.Appointment;
import com.lfhardware.appointment.dto.AppointmentInput;
import com.lfhardware.customer.mapper.CustomerMapper;
import com.lfhardware.provider.mapper.ServiceProviderMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {ServiceProviderMapper.class, CustomerMapper.class, AppointmentImageMapper.class,
        AddressMapper.class})
public interface AppointmentMapper {

    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "service", source = "service"),
            @Mapping(target = "customer", source = "customer"),
            @Mapping(target = "serviceProvider", source = "serviceProvider"),
            @Mapping(target = "createdAt", source = "appointmentId.createdAt"),
            @Mapping(target = "bookingDatetime", source = "bookingDatetime"),
            @Mapping(target = "estimatedPrice", source = "estimatedPrice"),
            @Mapping(target = "paid", source = "paid"),
            @Mapping(target = "status", source = "status"),
            @Mapping(target = "completionDateTime", source = "completionDateTime"),
            @Mapping(target = "statusLastUpdate", source = "statusLastUpdate"),
            @Mapping(target = "confirmationDatetime", source = "confirmationDatetime"),
            @Mapping(target = "jobStartedDatetime", source = "jobStartedDatetime"),
            @Mapping(target = "jobCompletionDatetime", source = "jobCompletionDatetime"),
            @Mapping(target = "reviewDatetime", source = "reviewDatetime"),
            @Mapping(target = "hasReview", source = "hasReview"),
            @Mapping(target = "address", source = "address"),
            @Mapping(target = "appointmentImages", source = "appointmentImages")
    })
    AppointmentDTO mapToAppointmentDTO(Appointment appointment);


    Appointment mapToAppointmentEntity(AppointmentInput appointmentInput);
}
