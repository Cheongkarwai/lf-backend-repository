package com.lfhardware.notification.service;

import com.lfhardware.auth.dto.UserDTO;
import com.lfhardware.auth.service.IUserService;
import com.lfhardware.notification.dto.NotificationDTO;
import com.lfhardware.notification.mapper.NotificationMapper;
import com.lfhardware.notification.repository.INotificationRepository;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.hibernate.SessionFactory;
import org.hibernate.reactive.stage.Stage;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.jmx.export.notification.UnableToSendNotificationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SignalType;
import reactor.core.publisher.Sinks;
import reactor.util.concurrent.Queues;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.util.stream.Collectors;

@Service
@Getter
public class NotificationService implements INotificationService {

    private final Sinks.Many<NotificationDTO> sink;

    private final INotificationRepository notificationRepository;

    private final Stage.SessionFactory sessionFactory;

    private final NotificationMapper notificationMapper;

    private final IUserService userService;


    public NotificationService(INotificationRepository notificationRepository,
                               Stage.SessionFactory sessionFactory,
                               NotificationMapper notificationMapper,
                               IUserService userService) {
        this.notificationRepository = notificationRepository;
        this.sessionFactory = sessionFactory;
        this.notificationMapper = notificationMapper;
        this.userService = userService;
        sink = Sinks.many()
                .multicast()
                .onBackpressureBuffer(Queues.SMALL_BUFFER_SIZE, false);
        ;
    }

    private Flux<NotificationDTO> findByUserId() {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .flatMap(authentication -> Mono.fromCompletionStage(
                        sessionFactory.withSession(session ->
                                notificationRepository.findByUserId(session, authentication.getName())
                                        .thenApply(notifications -> notifications.stream()
                                                .map(notification -> {
                                                    NotificationDTO notificationDTO = notificationMapper.mapToNotificationDTO(notification);
                                                    notificationDTO.setUserId(notification.getUserId());
                                                    return notificationDTO;
                                                })
                                                .collect(Collectors.toList())))))
                .flatMapIterable(notificationDTOs -> notificationDTOs);
    }


    public Mono<Void> save(NotificationDTO notificationDTO) {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .map(authentication -> {
                    notificationDTO.setUserId(authentication.getName());
                    return this.sink.tryEmitNext(notificationDTO);
                })
                .then();
    }

    public Mono<Void> send(String userId, String message) {
        NotificationDTO notificationDTO = new NotificationDTO(userId, message, LocalDateTime.now());

        return Mono.fromCompletionStage(sessionFactory.withTransaction((session, transaction) ->  notificationRepository.save(session, notificationMapper.mapToNotification(notificationDTO))))
                .then(Mono.fromCallable(()->this.sink.tryEmitNext(notificationDTO)))
                .then();
    }

    public Flux<ServerSentEvent<NotificationDTO>> findAll() {

        Mono<Authentication> authenticationMono = ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication);

        return Flux.merge(findByUserId(), sink.asFlux())
                .filterWhen(notificationDTO -> authenticationMono.map(authentication -> notificationDTO.getUserId()
                        .equals(authentication.getName())))
                .map(notificationDTO -> ServerSentEvent.builder(notificationDTO)
                        .retry(Duration.of(5, ChronoUnit.SECONDS))
                        .id("12")
                        .event("notification")
                        .build());

    }
}
