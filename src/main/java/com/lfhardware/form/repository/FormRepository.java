package com.lfhardware.form.repository;

import com.lfhardware.form.domain.Form;
import com.lfhardware.form.domain.FormId_;
import com.lfhardware.form.domain.Form_;
import com.lfhardware.form.dto.FormPageRequest;
import com.lfhardware.provider.domain.ServiceProvider_;
import com.lfhardware.provider_business.domain.Service_;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.reactive.stage.Stage;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletionStage;

@Repository
public class FormRepository implements IFormRepository{

    private final Stage.SessionFactory sessionFactory;

    public FormRepository(Stage.SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }
    @Override
    public CompletionStage<List<Form>> findAll(Stage.Session session) {
        return null;
    }

    @Override
    public CompletionStage<List<Form>> findAll(Stage.Session session, FormPageRequest pageRequest, List<Long> formIds){
        CriteriaBuilder criteriaBuilder = sessionFactory.getCriteriaBuilder();
        CriteriaQuery<Form> cq = criteriaBuilder.createQuery(Form.class);
        Root<Form> root = cq.from(Form.class);

        List<Predicate> predicates = new ArrayList<>();
//        predicates.add(criteriaBuilder.equal(root.get(ServiceProvider_.ID),pageRequest.getServiceProviderId()));
//        predicates.add(criteriaBuilder.equal(root.get(Service_.ID), pageRequest.getServiceId()));
//        predicates.add(root.get(FormId_.SERVICE_ID).in(formIds));
        cq.select(root);
        cq.where(predicates.toArray(Predicate[]::new));

        return session.createQuery(cq)
                .setFirstResult(pageRequest.getPage() * pageRequest.getPageSize())
                .setMaxResults(pageRequest.getPageSize())
                .getResultList();
    }

    @Override
    public CompletionStage<Long> count(Stage.Session session){
        CriteriaBuilder criteriaBuilder = sessionFactory.getCriteriaBuilder();
        CriteriaQuery<Long> cq = criteriaBuilder.createQuery(Long.class);
        Root<Form> root = cq.from(Form.class);

        cq.select(criteriaBuilder.count(root));

        return session.createQuery(cq)
                .getSingleResult();
    }

    @Override
    public CompletionStage<Long> countInIds(Stage.Session session, List<Long> formIds){
        CriteriaBuilder criteriaBuilder = sessionFactory.getCriteriaBuilder();
        CriteriaQuery<Long> cq = criteriaBuilder.createQuery(Long.class);
        Root<Form> root = cq.from(Form.class);


        cq.select(criteriaBuilder.count(root));
        cq.where(root.get(Form_.ID).in(formIds));

        return session.createQuery(cq)
                .getSingleResult();
    }

    @Override
    public CompletionStage<Form> findById(Stage.Session session, Long formId) {
        return session.find(Form.class,formId);
    }

    @Override
    public CompletionStage<Void> save(Stage.Session session, Form obj) {
        return session.persist(obj);
    }

    @Override
    public CompletionStage<List<Form>> findAllByIds(Stage.Session session, List<Long> formIds) {
        return null;
    }

    @Override
    public CompletionStage<Form> merge(Stage.Session session, Form obj) {
        return session.merge(obj);
    }

    @Override
    public CompletionStage<Void> deleteById(Stage.Session session, Long formId) {
        return null;
    }

    @Override
    public Form loadReferenceById(Stage.Session session, Long formId) {
        return null;
    }

    @Override
    public CompletionStage<Void> saveAll(Stage.Session session, Collection<Form> objs) {
        return null;
    }

    @Override
    public CompletionStage<Void> deleteAll(Stage.Session session, List<Form> objs) {
        return null;
    }

    @Override
    public CompletionStage<Void> delete(Stage.Session session, Form obj) {
        return null;
    }

    @Override
    public CompletionStage<Integer> deleteAllByIds(Stage.Session session, List<Long> formIds) {
        return null;
    }

}
