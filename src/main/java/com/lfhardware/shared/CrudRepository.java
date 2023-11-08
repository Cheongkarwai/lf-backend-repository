package com.lfhardware.shared;

import com.lfhardware.auth.domain.User;
import org.hibernate.reactive.stage.Stage;

import java.util.concurrent.CompletionStage;

public interface CrudRepository<T,ID> {

    CompletionStage<T> findById(Stage.Session session, ID id);

    CompletionStage<Void> save(Stage.Session session, User user);
}
