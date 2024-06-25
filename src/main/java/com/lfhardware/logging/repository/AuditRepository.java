package com.lfhardware.logging.repository;

import com.lfhardware.logging.domain.Audit;
import org.hibernate.reactive.stage.Stage;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletionStage;

@Repository
public class AuditRepository implements IAuditRepository{
    @Override
    public CompletionStage<List<Audit>> findAll(Stage.Session session) {
        return null;
    }

    @Override
    public CompletionStage<Audit> findById(Stage.Session session, Long aLong) {
        return null;
    }

    @Override
    public CompletionStage<Void> save(Stage.Session session, Audit obj) {
        return session.persist(obj);
    }

    @Override
    public CompletionStage<List<Audit>> findAllByIds(Stage.Session session, List<Long> longs) {
        return null;
    }

    @Override
    public CompletionStage<Audit> merge(Stage.Session session, Audit obj) {
        return null;
    }

    @Override
    public CompletionStage<Void> deleteById(Stage.Session session, Long aLong) {
        return null;
    }

    @Override
    public Audit loadReferenceById(Stage.Session session, Long aLong) {
        return null;
    }

    @Override
    public CompletionStage<Void> saveAll(Stage.Session session, Collection<Audit> objs) {
        return null;
    }

    @Override
    public CompletionStage<Void> deleteAll(Stage.Session session, List<Audit> objs) {
        return null;
    }

    @Override
    public CompletionStage<Void> delete(Stage.Session session, Audit obj) {
        return null;
    }

    @Override
    public CompletionStage<Integer> deleteAllByIds(Stage.Session session, List<Long> longs) {
        return null;
    }
}
