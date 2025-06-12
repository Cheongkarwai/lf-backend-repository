package com.lfhardware.provider.repository;

import com.lfhardware.provider.domain.ServiceProvider;
import com.lfhardware.provider.domain.Status;
import com.lfhardware.provider.dto.ServiceProviderCountGroupByDayDTO;
import com.lfhardware.core.repository.CrudRepository;
import com.lfhardware.core.dto.PageRequest;
import org.hibernate.reactive.stage.Stage;

import java.util.List;
import java.util.concurrent.CompletionStage;


public interface IProviderRepository extends CrudRepository<ServiceProvider,String> {


    CompletionStage<List<ServiceProvider>> findAll(Stage.Session session, PageRequest pageRequest, List<String> status,
                                                   List<String> states, Double rating, String serviceName);

    CompletionStage<Long> count(Stage.Session session, PageRequest request, List<String> status,
                                List<String> states, Double rating, String serviceName);

    CompletionStage<ServiceProvider> findDetailsById(Stage.Session session, String id);

    CompletionStage<Integer> updateStatus(Stage.Session session, String id, Status status);

    CompletionStage<List<ServiceProviderCountGroupByDayDTO>> countServiceProvidersGroupByDay(Stage.Session session, Integer day);
}
