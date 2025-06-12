package com.lfhardware.charges.repository;

import com.lfhardware.transaction.domain.Transaction;
import com.lfhardware.core.dto.PageRequest;
import com.lfhardware.transaction.domain.Transaction_;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.reactive.stage.Stage;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletionStage;

@Repository
public class TransactionRepository implements ITransactionRepository {

    private final Stage.SessionFactory sessionFactory;

    public TransactionRepository(Stage.SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public CompletionStage<List<Transaction>> findAll(Stage.Session session) {
        return null;
    }

    @Override
    public CompletionStage<Transaction> findById(Stage.Session session, String id) {
        return session.find(Transaction.class, id);
    }

    @Override
    public CompletionStage<Void> save(Stage.Session session, Transaction obj) {
        return null;
    }

    @Override
    public CompletionStage<List<Transaction>> findAllByIds(Stage.Session session, List<String> strings) {
        return null;
    }

    @Override
    public CompletionStage<Transaction> merge(Stage.Session session, Transaction obj) {
        return null;
    }

    @Override
    public CompletionStage<Void> deleteById(Stage.Session session, String s) {
        return null;
    }

    @Override
    public Transaction loadReferenceById(Stage.Session session, String s) {
        return null;
    }


    @Override
    public CompletionStage<Void> saveAll(Stage.Session session, Collection<Transaction> objs) {
        return null;
    }

    @Override
    public CompletionStage<Void> deleteAll(Stage.Session session, List<Transaction> objs) {
        return null;
    }

    @Override
    public CompletionStage<Void> delete(Stage.Session session, Transaction obj) {
        return null;
    }

    @Override
    public CompletionStage<Integer> deleteAllByIds(Stage.Session session, List<String> longs) {
        return null;
    }

    @Override
    public CompletionStage<List<Transaction>> findAll(Stage.Session session, PageRequest pageRequest) {
        CriteriaBuilder criteriaBuilder = sessionFactory.getCriteriaBuilder();
        CriteriaQuery<Transaction> cq = criteriaBuilder.createQuery(Transaction.class);
        Root<Transaction> root = cq.from(Transaction.class);

        List<Predicate> predicates = new ArrayList<>();

        if (Objects.nonNull(pageRequest.getSearch())){
            predicates.add(criteriaBuilder.or(
                    criteriaBuilder.like(root.get(Transaction_.CHARGE_AMOUNT).as(String.class), "%"+ pageRequest.getSearch()+"%"),
                    criteriaBuilder.like(root.get(Transaction_.CREATED_AT).as(String.class), "%"+ pageRequest.getSearch()+"%"),
                    criteriaBuilder.like(root.get(Transaction_.CURRENCY).as(String.class), "%"+ pageRequest.getSearch()+"%"),
                    criteriaBuilder.like(root.get(Transaction_.PAYMENT_METHOD).as(String.class), "%"+ pageRequest.getSearch()+"%")
            ));
        }

        cq.select(root).where(predicates.toArray(Predicate[]::new));
        return session.createQuery(cq).setFirstResult(pageRequest.getPageNo() * pageRequest.getPageSize())
                .setMaxResults(pageRequest.getPageSize())
                .getResultList();
    }

    @Override
    public CompletionStage<Long> count(Stage.Session session, PageRequest pageRequest) {
        CriteriaBuilder cb = sessionFactory.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Transaction> root = cq.from(Transaction.class);
        List<Predicate> predicates = new ArrayList<>();

        if (Objects.nonNull(pageRequest.getSearch())) {
            predicates.add(cb.or(
                    cb.like(root.get(Transaction_.CHARGE_AMOUNT).as(String.class), "%"+ pageRequest.getSearch() +"%"),
                    cb.like(root.get(Transaction_.CREATED_AT).as(String.class), "%"+ pageRequest.getSearch()+"%"),
                    cb.like(root.get(Transaction_.CURRENCY).as(String.class), "%"+ pageRequest.getSearch()+"%"),
                    cb.like(root.get(Transaction_.PAYMENT_METHOD).as(String.class), "%"+ pageRequest.getSearch()+"%")
            ));
        }

        cq.select(cb.count(root)).where(predicates.toArray(Predicate[]::new));
        return session.createQuery(cq)
                .getSingleResult();
    }
}
