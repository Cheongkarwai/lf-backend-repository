package com.lfhardware.auth.repository;

import com.lfhardware.auth.domain.UserRole;
import org.hibernate.reactive.stage.Stage;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletionStage;

@Repository
public class UserRoleRepository implements IUserRoleRepository{


    @Override
    public CompletionStage<List<UserRole>> findAll(Stage.Session session) {
        return null;
    }

    @Override
    public CompletionStage<UserRole> findById(Stage.Session session, Long aLong) {
        return null;
    }

    @Override
    public CompletionStage<Void> save(Stage.Session session, UserRole obj) {
        return null;
    }

    @Override
    public CompletionStage<List<UserRole>> findAllByIds(Stage.Session session, List<Long> longs) {
        return null;
    }

    @Override
    public CompletionStage<UserRole> merge(Stage.Session session, UserRole obj) {
        return null;
    }

    @Override
    public CompletionStage<Void> deleteById(Stage.Session session, Long aLong) {
        return null;
    }

    @Override
    public UserRole loadReferenceById(Stage.Session session, Long aLong) {
        return null;
    }

    @Override
    public CompletionStage<Void> saveAll(Stage.Session session, Collection<UserRole> objs) {
        return session.persist(objs.toArray());
    }

    @Override
    public CompletionStage<Void> deleteAll(Stage.Session session, List<UserRole> objs) {
        return null;
    }

    @Override
    public CompletionStage<Void> delete(Stage.Session session, UserRole obj) {
        return null;
    }

    @Override
    public CompletionStage<Integer> deleteAllByIds(Stage.Session session, List<Long> longs) {
        return null;
    }
}
