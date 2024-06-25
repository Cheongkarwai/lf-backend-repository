package com.lfhardware.appointment.service;

import com.lfhardware.appointment.domain.AppointmentCountGroupByDayDTO;
import com.lfhardware.appointment.domain.AppointmentId;
import com.lfhardware.appointment.domain.AppointmentStatus;
import com.lfhardware.appointment.dto.AppointmentDTO;
import com.lfhardware.appointment.dto.AppointmentFeesInput;
import com.lfhardware.appointment.dto.AppointmentInput;
import com.lfhardware.appointment.dto.AppointmentStatusInput;
import com.lfhardware.shared.PageInfo;
import com.lfhardware.shared.Pageable;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

public interface IAppointmentService {

    Mono<Pageable<AppointmentDTO>> findAll(PageInfo pageRequest, List<String> status);

    Mono<String> create(AppointmentInput appointmentInput);

    Mono<Void> updateStatus(AppointmentStatusInput appointmentStatusInput);

    Mono<String> payAppointment(AppointmentFeesInput appointmentFeesInput, String serviceId, String serviceProviderId,
                                String customerId, LocalDateTime createdAt);

    Mono<AppointmentDTO> findById(AppointmentId appointmentId);

    Mono<List<AppointmentCountGroupByDayDTO>> countAppointments(AppointmentStatus appointmentStatus, Integer integer);
}
