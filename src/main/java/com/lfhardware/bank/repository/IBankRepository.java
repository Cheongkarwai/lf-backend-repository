package com.lfhardware.bank.repository;

import com.lfhardware.provider.domain.Bank;
import com.lfhardware.shared.CrudRepository;
import org.hibernate.reactive.stage.Stage;

import java.util.concurrent.CompletionStage;

public interface IBankRepository extends CrudRepository<Bank,Long> {

    CompletionStage<Bank> findByName(Stage.Session session, String name);
}
