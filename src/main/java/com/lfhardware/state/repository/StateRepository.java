package com.lfhardware.state.repository;

import com.lfhardware.state.domain.State;
import org.hibernate.reactive.stage.Stage;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletionStage;

@Repository
public class StateRepository implements IStateRepository {

    private final Stage.SessionFactory sessionFactory;

   public StateRepository(Stage.SessionFactory sessionFactory){
       this.sessionFactory = sessionFactory;
   }

    @Override
    public CompletionStage<List<State>> findAll(Stage.Session session) {
        return session.createQuery("SELECT u FROM State u", State.class).getResultList();
    }

    @Override
    public CompletionStage<State> findById(Stage.Session session, Long aLong) {
        return null;
    }

    @Override
    public CompletionStage<Void> save(Stage.Session session, State user) {
        return null;
    }

    @Override
    public CompletionStage<List<State>> findAllByIds(Stage.Session session, List<Long> ids) {
        return session.find(State.class, ids.toArray());
    }

    @Override
    public CompletionStage<State> merge(Stage.Session session, State obj) {
        return null;
    }

    @Override
    public CompletionStage<Void> deleteById(Stage.Session session, Long aLong) {
        return null;
    }


    @Override
    public State loadReferenceById(Stage.Session session, Long aLong) {
        return null;
    }

    @Override
    public CompletionStage<Void> saveAll(Stage.Session session, Collection<State> objs) {
        return null;
    }

    @Override
    public CompletionStage<Void> deleteAll(Stage.Session session, List<State> objs) {
        return null;
    }

    @Override
    public CompletionStage<Void> delete(Stage.Session session, State obj) {
        return null;
    }

    @Override
    public CompletionStage<Integer> deleteAllByIds(Stage.Session session, List<Long> longs) {
        return null;
    }

    @Override
    public CompletionStage<List<State>> findAllByNames(Stage.Session session, List<String> names) {
        return session.createNamedQuery("State.findAllByNames", State.class)
                .setParameter("names", names)
                .getResultList();
    }
}
