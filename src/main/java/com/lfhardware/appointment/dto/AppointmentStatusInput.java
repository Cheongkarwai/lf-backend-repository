package com.lfhardware.appointment.dto;

import com.lfhardware.appointment.domain.AppointmentId;
import com.lfhardware.appointment.domain.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentStatusInput {

    private List<AppointmentIdInput> ids;

    private AppointmentStatus status;
}
