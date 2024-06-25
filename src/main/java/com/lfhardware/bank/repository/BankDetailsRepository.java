package com.lfhardware.bank.repository;

import com.lfhardware.provider.domain.BankDetailsId;
import com.lfhardware.provider.domain.BankingDetails;
import org.hibernate.reactive.stage.Stage;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletionStage;

@Repository
public class BankDetailsRepository implements IBankDetailsRepository{
    @Override
    public CompletionStage<List<BankingDetails>> findAll(Stage.Session session) {
        return null;
    }

    @Override
    public CompletionStage<BankingDetails> findById(Stage.Session session, BankDetailsId bankDetailsId) {
        return null;
    }

    @Override
    public CompletionStage<Void> save(Stage.Session session, BankingDetails obj) {
        return session.persist(obj);
    }

    @Override
    public CompletionStage<List<BankingDetails>> findAllByIds(Stage.Session session, List<BankDetailsId> bankDetailsIds) {
        return null;
    }

    @Override
    public CompletionStage<BankingDetails> merge(Stage.Session session, BankingDetails obj) {
        return null;
    }

    @Override
    public CompletionStage<Void> deleteById(Stage.Session session, BankDetailsId bankDetailsId) {
        return null;
    }

    @Override
    public BankingDetails loadReferenceById(Stage.Session session, BankDetailsId bankDetailsId) {
        return null;
    }

    @Override
    public CompletionStage<Void> saveAll(Stage.Session session, Collection<BankingDetails> objs) {
        return null;
    }

    @Override
    public CompletionStage<Void> deleteAll(Stage.Session session, List<BankingDetails> objs) {
        return null;
    }

    @Override
    public CompletionStage<Void> delete(Stage.Session session, BankingDetails obj) {
        return null;
    }

    @Override
    public CompletionStage<Integer> deleteAllByIds(Stage.Session session, List<BankDetailsId> bankDetailsIds) {
        return null;
    }
}
