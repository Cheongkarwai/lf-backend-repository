package com.lfhardware.shared;

import org.hibernate.reactive.stage.Stage;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletionStage;

public interface CrudRepository<T,ID> {

    CompletionStage<List<T>> findAll(Stage.Session session);

    CompletionStage<T> findById(Stage.Session session, ID id);

    CompletionStage<Void> save(Stage.Session session, T obj);

    CompletionStage<List<T>> findAllByIds(Stage.Session session, List<ID> ids);

    CompletionStage<T> merge(Stage.Session session, T obj);

    CompletionStage<Void> deleteById(Stage.Session session, ID id);

    T loadReferenceById(Stage.Session session, ID id);

    CompletionStage<Void> saveAll(Stage.Session session, Collection<T> objs);

    CompletionStage<Void> deleteAll(Stage.Session session, List<T> objs);

    CompletionStage<Void> delete(Stage.Session session,T obj);

    CompletionStage<Integer> deleteAllByIds(Stage.Session session, List<ID> ids);

}
