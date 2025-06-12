package com.lfhardware.customer.repository;

import com.lfhardware.customer.domain.Customer;
import com.lfhardware.customer.dto.CustomerCountGroupByDayDTO;
import com.lfhardware.core.repository.CrudRepository;
import com.lfhardware.core.dto.PageRequest;
import org.hibernate.reactive.stage.Stage;

import java.util.List;
import java.util.concurrent.CompletionStage;

public interface ICustomerRepository extends CrudRepository<Customer, String> {

    CompletionStage<List<Customer>> findAll(Stage.Session session, PageRequest pageRequest);

    CompletionStage<Long> count(Stage.Session session, PageRequest pageRequest);

    CompletionStage<List<CustomerCountGroupByDayDTO>> countCustomerGroupByDay(Stage.Session session, Integer day);
}
