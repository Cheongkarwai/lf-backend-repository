package com.lfhardware.notification.repository;

import com.lfhardware.customer.domain.Customer_;
import com.lfhardware.notification.domain.Notification;
import com.lfhardware.notification.domain.Notification_;
import com.lfhardware.provider.domain.ServiceProvider_;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.SessionFactory;
import org.hibernate.reactive.stage.Stage;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletionStage;

@Repository
public class NotificationRepository implements INotificationRepository{

    private final Stage.SessionFactory sessionFactory;

    public NotificationRepository(Stage.SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }
    @Override
    public CompletionStage<List<Notification>> findAll(Stage.Session session) {
        return null;
    }

    @Override
    public CompletionStage<Notification> findById(Stage.Session session, Long aLong) {
        return null;
    }

    @Override
    public CompletionStage<Void> save(Stage.Session session, Notification obj) {
        return session.persist(obj);
    }

    @Override
    public CompletionStage<List<Notification>> findAllByIds(Stage.Session session, List<Long> longs) {
        return null;
    }

    @Override
    public CompletionStage<Notification> merge(Stage.Session session, Notification obj) {
        return null;
    }

    @Override
    public CompletionStage<Void> deleteById(Stage.Session session, Long aLong) {
        return null;
    }

    @Override
    public Notification loadReferenceById(Stage.Session session, Long aLong) {
        return null;
    }

    @Override
    public CompletionStage<Void> saveAll(Stage.Session session, Collection<Notification> objs) {
        return null;
    }

    @Override
    public CompletionStage<Void> deleteAll(Stage.Session session, List<Notification> objs) {
        return null;
    }

    @Override
    public CompletionStage<Void> delete(Stage.Session session, Notification obj) {
        return null;
    }

    @Override
    public CompletionStage<Integer> deleteAllByIds(Stage.Session session, List<Long> longs) {
        return null;
    }

    @Override
    public CompletionStage<List<Notification>> findByUserId(Stage.Session session, String userId){
        CriteriaBuilder cb = sessionFactory.getCriteriaBuilder();
        CriteriaQuery<Notification> cq = cb.createQuery(Notification.class);
        Root<Notification> root = cq.from(Notification.class);

        cq.where(cb.equal(root.get(Notification_.USER_ID), userId));
        cq.orderBy(cb.desc(root.get(Notification_.createdAt)));
        cq.select(root);
        return session.createQuery(cq)
                .setMaxResults(5)
                .getResultList();
    }
//    @Override
//    public CompletionStage<List<Notification>> findByServiceProviderIdOrCustomerId(Stage.Session session, String userId) {
//        CriteriaBuilder cb = sessionFactory.getCriteriaBuilder();
//        CriteriaQuery<Notification> cq = cb.createQuery(Notification.class);
//        Root<Notification> root = cq.from(Notification.class);
//
//        cq.where(cb.or(cb.equal(root.get(Notification_.SERVICE_PROVIDER).get(ServiceProvider_.ID), userId),
//                cb.equal(root.get(Notification_.CUSTOMER).get(Customer_.ID), userId)));
//        cq.orderBy(cb.desc(root.get(Notification_.createdAt)));
//        cq.select(root);
//        return session.createQuery(cq)
//                .setMaxResults(5)
//                .getResultList();
//    }
}
