package com.lfhardware.notification.service;

import com.lfhardware.notification.dto.NotificationDTO;
import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface INotificationService {

    //Mono<Void> setNotification(NotificationDTO notificationDTO);

//    Flux<NotificationDTO> findByCurrentlyLoggedInUser();

    Mono<Void> save(NotificationDTO notificationDTO);

    Mono<Void> send(String userId, String message);

    Flux<ServerSentEvent<NotificationDTO>> findAll();
}
