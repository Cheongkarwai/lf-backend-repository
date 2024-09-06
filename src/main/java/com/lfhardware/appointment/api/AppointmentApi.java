package com.lfhardware.appointment.api;

import com.lfhardware.appointment.domain.AppointmentId;
import com.lfhardware.appointment.domain.AppointmentStatus;
import com.lfhardware.appointment.dto.AppointmentDTO;
import com.lfhardware.appointment.dto.AppointmentFeesInput;
import com.lfhardware.appointment.dto.AppointmentInput;
import com.lfhardware.appointment.dto.AppointmentStatusInput;
import com.lfhardware.appointment.service.IAppointmentService;
import com.lfhardware.shared.PageInfo;
import com.lfhardware.shared.PageQueryParameterBuilder;
import com.lfhardware.shared.Sort;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Appointment API
 * /api/appointments
 */
@Component
public class AppointmentApi {

    private final IAppointmentService appointmentService;

    public AppointmentApi(IAppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    /**
     * @param serverRequest - request object
     * @return Mono<ServerResponse>
     *
     */
    public Mono<ServerResponse> findAll(ServerRequest serverRequest) {

        PageInfo pageRequest = PageQueryParameterBuilder.buildPageRequest(serverRequest);

        List<String> status = serverRequest.queryParams().get("status");

        return appointmentService.findAll(pageRequest,status)
                .flatMap(appointmentPageable -> ServerResponse.ok()
                        .bodyValue(appointmentPageable));

    }

    /**
     * @param serverRequest - request object
     * @return Mono<ServerResponse>
     *
     */
    public Mono<ServerResponse> createAppointment(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(AppointmentInput.class)
                .flatMap(appointmentService::create)
                .flatMap(url -> ServerResponse.ok()
                        .bodyValue(url));
    }

    /**
     * @param serverRequest - request object
     * @return Mono<ServerResponse>
     *
     */
    public Mono<ServerResponse> updateAppointmentStatus(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(AppointmentStatusInput.class)
                .flatMap(appointmentService::updateStatus)
                .then(Mono.defer(() -> ServerResponse.noContent()
                        .build()));
    }

    /**
     * @param serverRequest - request object
     * @return Mono<ServerResponse>
     *
     */
    public Mono<ServerResponse> payAppointmentFees(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(AppointmentFeesInput.class)
                .flatMap(appointment -> appointmentService.payAppointment(appointment, serverRequest.pathVariable("serviceId"),
                        serverRequest.pathVariable("serviceProviderId"), serverRequest.pathVariable("customerId"),
                        LocalDateTime.parse(serverRequest.pathVariable("createdAt"))))
                .flatMap(e -> ServerResponse.ok()
                        .bodyValue(e));
    }

    /**
     * @param serverRequest - request object
     * @return Mono<ServerResponse>
     *
     */
    public Mono<ServerResponse> transferAppointmentFunds(ServerRequest serverRequest) {
        return appointmentService.transferAppointmentFunds(Long.valueOf(serverRequest.pathVariable("serviceId")),
                        serverRequest.pathVariable("serviceProviderId"), serverRequest.pathVariable("customerId"),
                        LocalDateTime.parse(serverRequest.pathVariable("createdAt")))
                .then(Mono.defer(()->ServerResponse.ok().build()));
    }

    /**
     * @param serverRequest - request object
     * @return Mono<ServerResponse>
     *
     */
    public Mono<ServerResponse> findById(ServerRequest serverRequest) {

        String serviceProviderId = serverRequest.pathVariable("serviceProviderId");
        String serviceId = serverRequest.pathVariable("serviceId");
        String customerId = serverRequest.pathVariable("customerId");
        String createdAt = serverRequest.pathVariable("createdAt");

        return appointmentService.findById(new AppointmentId(Long.valueOf(serviceId), serviceProviderId, customerId, LocalDateTime.parse(createdAt)))
                .flatMap(appointmentDTO -> ServerResponse.ok()
                        .bodyValue(appointmentDTO));
    }

    /**
     * @param serverRequest - request object
     * @return Mono<ServerResponse>
     *
     */
    public Mono<ServerResponse> count(ServerRequest serverRequest){

        String status = serverRequest.queryParam("status").orElse(null);
        String day = serverRequest.queryParam("day").orElse(null);

        return ServerResponse.ok()
                .body(appointmentService.countAppointments(
                        Objects.nonNull(status) ? AppointmentStatus.valueOf(status) : null,
                        Objects.nonNull(day) ? Integer.valueOf(day) : null), List.class);
    }

    /**
     * @param serverRequest - request object
     * @return Mono<ServerResponse>
     *
     */
    public Mono<ServerResponse> findAppointmentReceipt(ServerRequest serverRequest){
        String serviceProviderId = serverRequest.pathVariable("serviceProviderId");
        String serviceId = serverRequest.pathVariable("serviceId");
        String customerId = serverRequest.pathVariable("customerId");
        String createdAt = serverRequest.pathVariable("createdAt");

        return appointmentService.findReceiptById(new AppointmentId(Long.valueOf(serviceId), serviceProviderId, customerId, LocalDateTime.parse(createdAt)))
                .flatMap(receiptUrl -> ServerResponse.ok()
                        .bodyValue(receiptUrl));
    }
}
