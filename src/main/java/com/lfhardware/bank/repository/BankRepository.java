package com.lfhardware.bank.repository;

import com.lfhardware.provider.domain.Bank;
import org.hibernate.reactive.stage.Stage;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletionStage;

@Repository
public class BankRepository implements IBankRepository{

    private Stage.SessionFactory sessionFactory;
    public BankRepository(Stage.SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }
    @Override
    public CompletionStage<List<Bank>> findAll(Stage.Session session) {
        return null;
    }

    @Override
    public CompletionStage<Bank> findById(Stage.Session session, Long aLong) {
        return null;
    }

    @Override
    public CompletionStage<Void> save(Stage.Session session, Bank obj) {
        return null;
    }

    @Override
    public CompletionStage<List<Bank>> findAllByIds(Stage.Session session, List<Long> longs) {
        return null;
    }

    @Override
    public CompletionStage<Bank> merge(Stage.Session session, Bank obj) {
        return null;
    }

    @Override
    public CompletionStage<Void> deleteById(Stage.Session session, Long aLong) {
        return null;
    }

    @Override
    public Bank loadReferenceById(Stage.Session session, Long aLong) {
        return null;
    }

    @Override
    public CompletionStage<Void> saveAll(Stage.Session session, Collection<Bank> objs) {
        return null;
    }

    @Override
    public CompletionStage<Void> deleteAll(Stage.Session session, List<Bank> objs) {
        return null;
    }

    @Override
    public CompletionStage<Void> delete(Stage.Session session, Bank obj) {
        return null;
    }

    @Override
    public CompletionStage<Integer> deleteAllByIds(Stage.Session session, List<Long> longs) {
        return null;
    }

    @Override
    public CompletionStage<Bank> findByName(Stage.Session session, String name) {
        return session.createNamedQuery("Bank.findByName",Bank.class)
                .setParameter("name",name)
                .getSingleResult();
    }
}
