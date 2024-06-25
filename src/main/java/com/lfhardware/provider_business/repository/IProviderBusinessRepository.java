package com.lfhardware.provider_business.repository;

import com.lfhardware.provider_business.domain.Service;
import com.lfhardware.provider_business.domain.ServiceCategory;
import com.lfhardware.provider_business.dto.ServiceGroupByCategoryDTO;
import com.lfhardware.shared.CrudRepository;
import org.hibernate.reactive.stage.Stage;

import java.util.List;
import java.util.concurrent.CompletionStage;

public interface IProviderBusinessRepository extends CrudRepository<Service,Long> {
    CompletionStage<List<ServiceCategory>> findAllGroupByCategory(Stage.Session session);

}
