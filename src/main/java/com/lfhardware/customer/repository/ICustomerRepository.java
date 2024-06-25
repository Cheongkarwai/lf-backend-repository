package com.lfhardware.customer.repository;

import com.lfhardware.customer.domain.Customer;
import com.lfhardware.customer.dto.CustomerCountGroupByDayDTO;
import com.lfhardware.shared.CrudRepository;
import com.lfhardware.shared.PageInfo;
import org.hibernate.reactive.stage.Stage;

import java.util.List;
import java.util.concurrent.CompletionStage;

public interface ICustomerRepository extends CrudRepository<Customer, String> {

    CompletionStage<List<Customer>> findAll(Stage.Session session, PageInfo pageRequest);

    CompletionStage<Long> count(Stage.Session session, PageInfo pageRequest);

    CompletionStage<List<CustomerCountGroupByDayDTO>> countCustomerGroupByDay(Stage.Session session, Integer day);
}
