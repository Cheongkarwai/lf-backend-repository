package com.lfhardware.auth.repository;

import com.lfhardware.auth.domain.Role;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.Persistence;
import org.hibernate.reactive.stage.Stage;
import org.springframework.stereotype.Repository;

import java.util.Collection;
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
    public CompletionStage<List<Role>> findAll(Stage.Session session) {
        return session.createNamedQuery("Role.findAll", Role.class)
                .getResultList();
    }

    @Override
    public CompletionStage<Role> findById(Stage.Session session, Long aLong) {
        return null;
    }

    @Override
    public CompletionStage<Void> save(Stage.Session session, Role obj) {
        return null;
    }

    @Override
    public CompletionStage<List<Role>> findAllByIds(Stage.Session session, List<Long> ids) {
        return session.find(Role.class,ids.toArray(Long[]::new));

    }

    @Override
    public CompletionStage<Role> merge(Stage.Session session, Role obj) {
        return null;
    }

    @Override
    public CompletionStage<Void> deleteById(Stage.Session session, Long aLong) {
        return null;
    }

    @Override
    public Role loadReferenceById(Stage.Session session, Long aLong) {
        return null;
    }

    @Override
    public CompletionStage<Void> saveAll(Stage.Session session, Collection<Role> objs) {
        return null;
    }

    @Override
    public CompletionStage<Void> deleteAll(Stage.Session session, List<Role> objs) {
        return null;
    }

    @Override
    public CompletionStage<Void> delete(Stage.Session session, Role obj) {
        return null;
    }

    @Override
    public CompletionStage<Integer> deleteAllByIds(Stage.Session session, List<Long> longs) {
        return null;
    }

    @Override
    public CompletionStage<Role> findByName(Stage.Session session,String name) {
        return session.createNamedQuery("Role.findByName",Role.class).setParameter("name",name)
                .getSingleResult();


    }

    @Override
    public CompletionStage<List<Role>> findByNameIn(Stage.Session session, List<String> names){
        return session.createQuery("FROM Role r WHERE r.name IN (:names)", Role.class)
                .setParameter("names", names)
                .getResultList();
    }
}
