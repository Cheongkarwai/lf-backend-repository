package com.lfhardware.appointment.mapper;

import com.lfhardware.appointment.domain.AppointmentImage;
import com.lfhardware.appointment.dto.AppointmentImageDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AppointmentImageMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "path", target = "path")
    AppointmentImageDTO mapToAppointmentImageDTO(AppointmentImage appointmentImage);
}
