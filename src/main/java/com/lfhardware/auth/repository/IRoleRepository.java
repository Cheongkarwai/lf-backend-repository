package com.lfhardware.auth.repository;

import com.lfhardware.auth.domain.Role;
import org.hibernate.reactive.stage.Stage;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public interface IRoleRepository {

    CompletableFuture<Role> findById(Long id, CompletionStage<Stage.Session> session);

    CompletionStage<Role> findById(Long id, Stage.Session session);

    CompletionStage<List<Role>> findAllByIds(Stage.Session session, List<Long> ids);
}
