package com.lfhardware.auth.repository;

import com.lfhardware.auth.domain.Role;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.Persistence;
import org.hibernate.reactive.stage.Stage;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Repository
public class RoleRepository implements  IRoleRepository{

    private Stage.SessionFactory stagedSessionFactory;

    @PostConstruct
    public void init(){
        stagedSessionFactory = Persistence.createEntityManagerFactory("postgres").unwrap(Stage.SessionFactory.class);
    }

    @Override
    public CompletableFuture<Role> findById(Long id, CompletionStage<Stage.Session> session) {
        return null;
    }

    @Override
    public CompletionStage<Role> findById(Long id, Stage.Session session) {
        return session.find(Role.class,id);

    }

    @Override
    public CompletionStage<List<Role>> findAllByIds(Stage.Session session, List<Long> ids) {
        return session.find(Role.class,ids.toArray(Long[]::new));

    }
}
