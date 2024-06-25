package com.lfhardware.auth.repository;

import com.lfhardware.auth.domain.User;
import com.lfhardware.auth.dto.DailyUserStat;
import com.lfhardware.auth.dto.UserPageRequest;
import com.lfhardware.shared.CrudRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.hibernate.reactive.stage.Stage;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public interface IUserRepository extends CrudRepository<User,String> {

    CompletionStage<List<User>> findAll(Stage.Session session, UserPageRequest userPageRequest);

    CompletionStage<Long> count(Stage.Session session, UserPageRequest userPageRequest);

    CompletionStage<User> findUserRoleById(Stage.Session session, String username);

    CompletionStage<User> findByEmailAddress(Stage.Session session, String emailAddress);

    CompletionStage<User> merge(Stage.Session session, User user);

    CompletionStage<List<DailyUserStat>> countDailyUserGroupByDays(Stage.Session session, Integer days) ;
}
