package com.lfhardware.notification.api;

import com.lfhardware.notification.dto.NotificationDTO;
import com.lfhardware.notification.service.INotificationService;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.*;

//@Component
//public class NotificationHandler {
//
//    //private FluxProcessor<NotificationDTO, NotificationDTO> processor;
//    private final NotificationService notificationService;
//
//    public NotificationHandler(NotificationService notificationService) {
//        this.notificationService = notificationService;
//    }
//
//    public Mono<ServerResponse> streamNotification(ServerRequest serverRequest) {
//        return ServerResponse.ok().body(notificationService.getNotifications(), ServerSentEvent.class);
//    }
//
//    public Mono<ServerResponse> createNotification(ServerRequest serverRequest) {
//        return serverRequest.bodyToMono(NotificationDTO.class)
//                .map(notificationService::setNotification)
//                .flatMap(e -> ServerResponse.ok().build());
//    }
//}

//@RestController
@Component
public class  NotificationApi{

    private final INotificationService notificationService;
    public NotificationApi(INotificationService notificationService) {
        this.notificationService = notificationService;
    }

//    @GetMapping(value = "/api/v1/notifications")
//    public Flux<NotificationDTO> findCurrentUserNotification(){
//         return notificationService.findCurrentUserNotification();
//                //.onErrorResume(()-> ServerSentEvent.builder("helo").build());
//    }
//    public Mono<ServerResponse> findCurrentlyLoggedInUserNotification(){
//        return ServerResponse.ok()
//                .bodyValue(notificationService.findCurrentUserNotification());
//    }

    public Mono<ServerResponse> findAll(ServerRequest serverRequest){
        return ServerResponse.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(notificationService.findAll(), ServerSentEvent.class);

    }

    public Mono<ServerResponse> save(ServerRequest serverRequest){
        return serverRequest.bodyToMono(NotificationDTO.class)
                .flatMap(notificationService::save)
                .then(ServerResponse.noContent().build());
    }

//    @PostMapping("/api/v1/notifications")
//    public Mono<Void> sendNotification(@RequestBody NotificationDTO notificationDTO){
//        return notificationService.setNotification(notificationDTO);
//    }

}
