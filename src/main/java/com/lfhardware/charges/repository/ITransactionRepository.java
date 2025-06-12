package com.lfhardware.charges.repository;

import com.lfhardware.transaction.domain.Transaction;
import com.lfhardware.core.repository.CrudRepository;
import com.lfhardware.core.dto.PageRequest;
import org.hibernate.reactive.stage.Stage;

import java.util.List;
import java.util.concurrent.CompletionStage;

public interface ITransactionRepository extends CrudRepository<Transaction,String> {

    CompletionStage<List<Transaction>> findAll(Stage.Session session, PageRequest pageRequest);

    CompletionStage<Long> count(Stage.Session session, PageRequest pageRequest);
}
