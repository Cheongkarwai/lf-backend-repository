package com.lfhardware.provider.repository;

import com.lfhardware.provider.domain.ServiceDetails;
import com.lfhardware.provider.domain.ServiceDetails_;
import com.lfhardware.provider.domain.ServiceProvider_;
import com.lfhardware.provider_business.domain.Service_;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.reactive.stage.Stage;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletionStage;

@Repository
public class ServiceDetailsRepository implements IServiceDetailsRepository{

    private final Stage.SessionFactory sessionFactory;
    public ServiceDetailsRepository(Stage.SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    @Override
    public CompletionStage<List<ServiceDetails>> findAllByServiceProviderId(String serviceProviderId, Stage.Session session) {
        CriteriaBuilder cb = sessionFactory.getCriteriaBuilder();
        CriteriaQuery<ServiceDetails> cq = cb.createQuery(ServiceDetails.class);
        Root<ServiceDetails> root = cq.from(ServiceDetails.class);
        cq.where(cb.equal(root.get(ServiceDetails_.SERVICE_PROVIDER).get(ServiceProvider_.ID), serviceProviderId));
        cq.select(root);
        return session.createQuery(cq)
                .getResultList();
    }

    @Override
    public CompletionStage<Integer> deleteByServiceProviderId(Stage.Session session, String serviceProviderId) {
        CriteriaBuilder cb = sessionFactory.getCriteriaBuilder();
        CriteriaDelete<ServiceDetails> cd = cb.
                createCriteriaDelete(ServiceDetails.class);
        Root<ServiceDetails> root = cd.from(ServiceDetails.class);
        cd.where(cb.equal(root.get(ServiceDetails_.SERVICE_PROVIDER).get(ServiceProvider_.ID), serviceProviderId));
        return session.createQuery(cd)
                .executeUpdate();

    }

    @Override
    public CompletionStage<List<ServiceDetails>> findAll(Stage.Session session) {
        return null;
    }

    @Override
    public CompletionStage<ServiceDetails> findById(Stage.Session session, Long aLong) {
        return null;
    }

    @Override
    public CompletionStage<Void> save(Stage.Session session, ServiceDetails obj) {
        return null;
    }

    @Override
    public CompletionStage<List<ServiceDetails>> findAllByIds(Stage.Session session, List<Long> longs) {
        return null;
    }

    @Override
    public CompletionStage<ServiceDetails> merge(Stage.Session session, ServiceDetails obj) {
        return null;
    }

    @Override
    public CompletionStage<Void> deleteById(Stage.Session session, Long aLong) {
        return null;
    }

    @Override
    public ServiceDetails loadReferenceById(Stage.Session session, Long aLong) {
        return null;
    }

    @Override
    public CompletionStage<Void> saveAll(Stage.Session session, Collection<ServiceDetails> objs) {
        return null;
    }

    @Override
    public CompletionStage<Void> deleteAll(Stage.Session session, List<ServiceDetails> objs) {
        return null;
    }

    @Override
    public CompletionStage<Void> delete(Stage.Session session, ServiceDetails obj) {
        return null;
    }

    @Override
    public CompletionStage<Integer> deleteAllByIds(Stage.Session session, List<Long> longs) {
        return null;
    }
}
