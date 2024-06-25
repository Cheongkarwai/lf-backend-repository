package com.lfhardware.state.repository;

import com.lfhardware.state.domain.State;
import com.lfhardware.shared.CrudRepository;
import org.hibernate.reactive.stage.Stage;

import java.util.List;
import java.util.concurrent.CompletionStage;

public interface IStateRepository extends CrudRepository<State,Long> {

    CompletionStage<List<State>> findAllByNames(Stage.Session session, List<String> names);
}
