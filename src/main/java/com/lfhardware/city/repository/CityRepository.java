package com.lfhardware.city.repository;

import com.lfhardware.city.domain.City;
import org.hibernate.reactive.stage.Stage;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletionStage;

@Repository
public class CityRepository implements ICityRepository{

    private final Stage.SessionFactory sessionFactory;

    public CityRepository(Stage.SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    @Override
    public CompletionStage<List<City>> findAll(Stage.Session session) {
        return session.createQuery("SELECT u FROM City u",City.class).getResultList();
    }

    @Override
    public CompletionStage<City> findById(Stage.Session session, Long aLong) {
        return null;
    }

    @Override
    public CompletionStage<Void> save(Stage.Session session, City user) {
        return null;
    }

    @Override
    public CompletionStage<List<City>> findAllByIds(Stage.Session session, List<Long> longs) {
        return null;
    }

    @Override
    public CompletionStage<City> merge(Stage.Session session, City obj) {
        return null;
    }

    @Override
    public CompletionStage<Void> deleteById(Stage.Session session, Long aLong) {
        return null;
    }


    @Override
    public City loadReferenceById(Stage.Session session, Long aLong) {
        return null;
    }

    @Override
    public CompletionStage<Void> saveAll(Stage.Session session, Collection<City> objs) {
        return null;
    }

    @Override
    public CompletionStage<Void> deleteAll(Stage.Session session, List<City> objs) {
        return null;
    }

    @Override
    public CompletionStage<Void> delete(Stage.Session session, City obj) {
        return null;
    }

    @Override
    public CompletionStage<Integer> deleteAllByIds(Stage.Session session, List<Long> longs) {
        return null;
    }

    @Override
    public CompletionStage<List<City>> findAllByNames(Stage.Session session, List<String> names) {
        return session.createNamedQuery("City.findAllByNames", City.class)
                .setParameter("names", names)
                .getResultList();
    }
}
