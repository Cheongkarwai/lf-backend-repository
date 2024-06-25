package com.lfhardware.provider.repository;

import com.lfhardware.provider.domain.ServiceDetails;
import com.lfhardware.shared.CrudRepository;
import org.hibernate.reactive.stage.Stage;

import java.util.List;
import java.util.concurrent.CompletionStage;

public interface IServiceDetailsRepository extends CrudRepository<ServiceDetails, Long> {
    CompletionStage<List<ServiceDetails>> findAllByServiceProviderId(String serviceProviderId, Stage.Session session);

    CompletionStage<Integer> deleteByServiceProviderId(Stage.Session session, String serviceProviderId);
}
