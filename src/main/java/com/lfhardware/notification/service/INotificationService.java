package com.lfhardware.notification.service;

import com.lfhardware.appointment.domain.AppointmentStatus;
import com.lfhardware.notification.dto.NotificationDTO;
import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.CompletionStage;

public interface INotificationService {

    //Mono<Void> setNotification(NotificationDTO notificationDTO);

//    Flux<NotificationDTO> findByCurrentlyLoggedInUser();

    Mono<Void> save(NotificationDTO notificationDTO);

    Mono<Void> send(String userId, String message);

    Flux<ServerSentEvent<NotificationDTO>> findAll();

    Mono<Void> sendBookingAppointmentNotification(String receiverId, String appointmentId, String serviceProviderName, String serviceName);


    Mono<Void> sendAppointmentUpdateNotification(String receiverId, AppointmentStatus appointmentStatus, UUID appointmentId);

    Mono<Void> sendPaymentCompletedNotification(String receiverId, String serviceProviderName, String serviceName, BigDecimal amount);

    Mono<Void> sendProfileUpdatedNotification(String receiverId);

    Mono<Void> sendTransferFundCompletedNotification(String adminId, BigDecimal estimatedPrice, UUID id);

    Mono<Void> sendReceiveFundsNotification(String serviceProviderId, BigDecimal estimatedPrice, UUID id);
}
