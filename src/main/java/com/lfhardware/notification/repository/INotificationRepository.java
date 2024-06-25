package com.lfhardware.notification.repository;

import com.lfhardware.notification.domain.Notification;
import com.lfhardware.shared.CrudRepository;
import org.hibernate.reactive.stage.Stage;

import java.util.List;
import java.util.concurrent.CompletionStage;

public interface INotificationRepository extends CrudRepository<Notification, Long> {

//    CompletionStage<List<Notification>> findByServiceProviderIdOrCustomerId(Stage.Session session, String userId);

    CompletionStage<List<Notification>> findByUserId(Stage.Session session, String userId);
}
