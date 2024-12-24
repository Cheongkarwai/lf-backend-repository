package com.lfhardware.country.repository;

import com.lfhardware.country.domain.Country;
import com.lfhardware.core.repository.CrudRepository;
import org.hibernate.reactive.stage.Stage;

import java.util.List;
import java.util.concurrent.CompletionStage;

public interface ICountryRepository extends CrudRepository<Country,Long> {
    CompletionStage<List<Country>> findAllByNames(Stage.Session session, List<String> names);
}
