package com.lfhardware.provider_business.repository;

import com.lfhardware.provider_business.domain.ServiceCategory;
import org.hibernate.reactive.stage.Stage;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletionStage;

@Repository
public class ProviderBusinessCategoryRepository implements IProviderBusinessCategoryRepository{

    private Stage.SessionFactory sessionFactory;
    public ProviderBusinessCategoryRepository(Stage.SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }
    @Override
    public CompletionStage<List<ServiceCategory>> findAll(Stage.Session session) {
        return null;
    }

    @Override
    public CompletionStage<ServiceCategory> findById(Stage.Session session, Long aLong) {
        return null;
    }

    @Override
    public CompletionStage<Void> save(Stage.Session session, ServiceCategory obj) {
        return null;
    }

    @Override
    public CompletionStage<List<ServiceCategory>> findAllByIds(Stage.Session session, List<Long> ids) {
        return null;
    }

    @Override
    public CompletionStage<ServiceCategory> merge(Stage.Session session, ServiceCategory obj) {
        return null;
    }

    @Override
    public CompletionStage<Void> deleteById(Stage.Session session, Long aLong) {
        return null;
    }

    @Override
    public ServiceCategory loadReferenceById(Stage.Session session, Long aLong) {
        return null;
    }

    @Override
    public CompletionStage<Void> saveAll(Stage.Session session, Collection<ServiceCategory> objs) {
        return null;
    }

    @Override
    public CompletionStage<Void> deleteAll(Stage.Session session, List<ServiceCategory> objs) {
        return null;
    }

    @Override
    public CompletionStage<Void> delete(Stage.Session session, ServiceCategory obj) {
        return null;
    }

    @Override
    public CompletionStage<Integer> deleteAllByIds(Stage.Session session, List<Long> longs) {
        return null;
    }
}
