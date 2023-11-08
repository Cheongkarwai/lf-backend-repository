package com.lfhardware.auth.repository;

import com.lfhardware.auth.domain.User;
import com.lfhardware.shared.CrudRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.hibernate.reactive.stage.Stage;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public interface IUserRepository extends CrudRepository<User,String> {


    CompletionStage<User> findUserRoleById(Stage.Session session, String username);

    CompletionStage<User> findByEmailAddress(Stage.Session session, String emailAddress);

    CompletionStage<User> merge(Stage.Session session, User user);
}
