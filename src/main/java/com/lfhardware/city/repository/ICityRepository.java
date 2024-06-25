package com.lfhardware.city.repository;

import com.lfhardware.city.domain.City;
import com.lfhardware.shared.CrudRepository;
import com.lfhardware.state.domain.State;
import org.hibernate.reactive.stage.Stage;

import java.util.List;
import java.util.concurrent.CompletionStage;

public interface ICityRepository extends CrudRepository<City,Long> {

    CompletionStage<List<City>> findAllByNames(Stage.Session session, List<String> names);
}
