package com.lfhardware.provider_business.repository;

import com.lfhardware.provider.domain.ServiceProvider_;
import com.lfhardware.provider_business.domain.Service;
import com.lfhardware.provider_business.domain.ServiceCategory;
import com.lfhardware.provider_business.domain.Service_;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.reactive.stage.Stage;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletionStage;

@Repository
public class ProviderBusinessRepository implements IProviderBusinessRepository {

    private final Stage.SessionFactory sessionFactory;


    public ProviderBusinessRepository(Stage.SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public CompletionStage<List<Service>> findAll(Stage.Session session) {
        return session.createQuery("SELECT u FROM Service u JOIN FETCH u.serviceDetails", Service.class).getResultList();
    }

    @Override
    public CompletionStage<Service> findById(Stage.Session session, Long id) {
        return session.find(Service.class, id);
    }

    @Override
    public CompletionStage<Void> save(Stage.Session session, Service obj) {
        return null;
    }

    @Override
    public CompletionStage<List<Service>> findAllByIds(Stage.Session session, List<Long> ids) {
        return session.find(Service.class, ids.toArray());
    }

    @Override
    public CompletionStage<Service> merge(Stage.Session session, Service obj) {
        return null;
    }

    @Override
    public CompletionStage<Void> deleteById(Stage.Session session, Long aLong) {
        return null;
    }

    @Override
    public Service loadReferenceById(Stage.Session session, Long aLong) {
        return null;
    }

    @Override
    public CompletionStage<Void> saveAll(Stage.Session session, Collection<Service> objs) {
        return null;
    }

    @Override
    public CompletionStage<Void> deleteAll(Stage.Session session, List<Service> objs) {
        return null;
    }

    @Override
    public CompletionStage<Void> delete(Stage.Session session, Service obj) {
        return null;
    }

    @Override
    public CompletionStage<Integer> deleteAllByIds(Stage.Session session, List<Long> longs) {
        return null;
    }

    @Override
    public CompletionStage<List<ServiceCategory>> findAllGroupByCategory(Stage.Session session) {
        return session.createQuery("SELECT u FROM ServiceCategory u", ServiceCategory.class)
                .setPlan(session.getEntityGraph(ServiceCategory.class, "ServiceCategory.services"))
                .getResultList();
    }


}
