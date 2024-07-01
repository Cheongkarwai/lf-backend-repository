package com.lfhardware.appointment.repository;

import com.lfhardware.appointment.domain.Appointment;
import com.lfhardware.appointment.dto.AppointmentCountGroupByDayDTO;
import com.lfhardware.appointment.domain.AppointmentId;
import com.lfhardware.appointment.domain.AppointmentStatus;
import com.lfhardware.provider.dto.ServiceProviderAppointmentCountGroupByDayDTO;
import com.lfhardware.shared.CrudRepository;
import com.lfhardware.shared.PageInfo;
import org.hibernate.reactive.stage.Stage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletionStage;

public interface IAppointmentRepository extends CrudRepository<Appointment, AppointmentId> {

    CompletionStage<List<Appointment>> findAll(Stage.Session session, PageInfo pageInfo, List<String> status);

    CompletionStage<Long> count(Stage.Session session, PageInfo pageInfo, List<String> status);

    CompletionStage<List<Appointment>> findAllByServiceProviderId(Stage.Session session, PageInfo pageInfo, String serviceProviderId, List<String> status);

    CompletionStage<Long> countByServiceProviderId(Stage.Session session, PageInfo pageInfo, String serviceProviderId, List<String> status);

    CompletionStage<List<Appointment>> findAllByCustomerId(Stage.Session session, PageInfo pageRequest, String customerId);
    CompletionStage<Long> countByCustomerId(Stage.Session session, PageInfo pageInfo, String customerId);

    CompletionStage<List<Appointment>> findAllByCustomerIdAndBookingDateAndStatus(Stage.Session session, PageInfo pageRequest, String customerId, LocalDateTime dateTime, List<String> status);

    CompletionStage<Long> countByCustomerIdAndBookingDateAndStatus(Stage.Session session, PageInfo pageRequest, String customerId, LocalDateTime dateTime, List<String> status);

    CompletionStage<List<ServiceProviderAppointmentCountGroupByDayDTO>> countAppointmentsByServiceProviderIdGroupByDay(Stage.Session session, String serviceProviderId, AppointmentStatus status,
                                                                                                                       Integer day);

    CompletionStage<List<AppointmentCountGroupByDayDTO>> countAppointmentsGroupByDay(Stage.Session session, AppointmentStatus appointmentStatus,Integer day);

    CompletionStage<Appointment> findByCheckoutSessionId(Stage.Session session, String checkoutSessionId);
}
