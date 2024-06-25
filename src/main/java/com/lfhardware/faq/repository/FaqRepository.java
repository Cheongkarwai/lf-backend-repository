package com.lfhardware.faq.repository;

import com.lfhardware.faq.domain.Faq;
import com.lfhardware.faq.domain.Faq_;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.reactive.stage.Stage;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletionStage;

@Repository
@Slf4j
public class FaqRepository implements IFaqRepository {

    private final Stage.SessionFactory sessionFactory;

    public FaqRepository(Stage.SessionFactory sessionFactory, EntityManager em) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public CompletionStage<List<Faq>> findAll(Stage.Session session) {
        CriteriaBuilder cb = sessionFactory.getCriteriaBuilder();
        CriteriaQuery<Faq> cq = cb.createQuery(Faq.class);
        Root<Faq> root = cq.from(Faq.class);

        cq.select(root);

        return session.createQuery(cq)
                .getResultList();
    }

    @Override
    public CompletionStage<Faq> findById(Stage.Session session, Long aLong) {
        return null;
    }

    @Override
    public CompletionStage<Void> save(Stage.Session session, Faq obj) {
        return session.persist(obj);
    }

    @Override
    public CompletionStage<List<Faq>> findAllByIds(Stage.Session session, List<Long> longs) {
        return null;
    }

    @Override
    public CompletionStage<Faq> merge(Stage.Session session, Faq obj) {
        return null;
    }

    @Override
    public CompletionStage<Void> deleteById(Stage.Session session, Long id) {
        CriteriaBuilder cb = sessionFactory.getCriteriaBuilder();
        CriteriaDelete<Faq> cd = cb.createCriteriaDelete(Faq.class);
        Root<Faq> root = cd.from(Faq.class);
        cd.where(cb.equal(root.get(Faq_.ID), id));
        return session.createQuery(cd)
                .executeUpdate().thenAccept((e)-> log.info("FAQ {} is deleted", id));
    }

    @Override
    public Faq loadReferenceById(Stage.Session session, Long aLong) {
        return null;
    }

    @Override
    public CompletionStage<Void> saveAll(Stage.Session session, Collection<Faq> objs) {
        return null;
    }

    @Override
    public CompletionStage<Void> deleteAll(Stage.Session session, List<Faq> objs) {
        return null;
    }

    @Override
    public CompletionStage<Void> delete(Stage.Session session, Faq obj) {
        return null;
    }

    @Override
    public CompletionStage<Integer> deleteAllByIds(Stage.Session session, List<Long> longs) {
        return null;
    }
}
