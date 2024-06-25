package com.lfhardware.charges.repository;

import com.lfhardware.transaction.domain.Transaction;
import com.lfhardware.shared.CrudRepository;
import com.lfhardware.shared.PageInfo;
import org.hibernate.reactive.stage.Stage;

import java.util.List;
import java.util.concurrent.CompletionStage;

public interface ITransactionRepository extends CrudRepository<Transaction,String> {

    CompletionStage<List<Transaction>> findAll(Stage.Session session, PageInfo pageInfo);

    CompletionStage<Long> count(Stage.Session session, PageInfo pageInfo);
}
