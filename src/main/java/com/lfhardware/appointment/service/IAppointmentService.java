package com.lfhardware.appointment.service;

import com.lfhardware.appointment.dto.AppointmentCountGroupByDayDTO;
import com.lfhardware.appointment.domain.AppointmentId;
import com.lfhardware.appointment.domain.AppointmentStatus;
import com.lfhardware.appointment.dto.AppointmentDTO;
import com.lfhardware.appointment.dto.AppointmentFeesInput;
import com.lfhardware.appointment.dto.AppointmentInput;
import com.lfhardware.appointment.dto.AppointmentStatusInput;
import com.lfhardware.core.dto.PageRequest;
import com.lfhardware.core.dto.Page;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

public interface IAppointmentService {

    Mono<Page<AppointmentDTO>> findAll(PageRequest pageRequest, List<String> status);

    Mono<String> create(AppointmentInput appointmentInput);

    Mono<Void> updateStatus(AppointmentStatusInput appointmentStatusInput);

    Mono<String> payAppointment(AppointmentFeesInput appointmentFeesInput, String serviceId, String serviceProviderId,
                                String customerId, LocalDateTime createdAt);

    Mono<AppointmentDTO> findById(AppointmentId appointmentId);

    Mono<List<AppointmentCountGroupByDayDTO>> countAppointments(AppointmentStatus appointmentStatus, Integer integer);

    Mono<Void> transferAppointmentFunds(Long serviceId, String serviceProviderId, String customerId, LocalDateTime createdAt);

    Mono<Void> fulfillAppointment(String sessionId, String paymentIntentId);

    Mono<String> findReceiptById(AppointmentId appointmentId);
}
