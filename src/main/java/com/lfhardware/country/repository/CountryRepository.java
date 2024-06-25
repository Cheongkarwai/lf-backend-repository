package com.lfhardware.country.repository;

import com.lfhardware.country.domain.Country;
import org.hibernate.reactive.stage.Stage;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletionStage;

@Repository
public class CountryRepository implements ICountryRepository {

    private Stage.SessionFactory sessionFactory;
    public CountryRepository(Stage.SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    @Override
    public CompletionStage<List<Country>> findAll(Stage.Session session) {
        return session.createQuery("SELECT u FROM Country u", Country.class).getResultList();
    }

    @Override
    public CompletionStage<Country> findById(Stage.Session session, Long aLong) {
        return null;
    }

    @Override
    public CompletionStage<Void> save(Stage.Session session, Country obj) {
        return null;
    }

    @Override
    public CompletionStage<List<Country>> findAllByIds(Stage.Session session, List<Long> longs) {
        return null;
    }

    @Override
    public CompletionStage<Country> merge(Stage.Session session, Country obj) {
        return null;
    }

    @Override
    public CompletionStage<Void> deleteById(Stage.Session session, Long aLong) {
        return null;
    }


    @Override
    public Country loadReferenceById(Stage.Session session, Long aLong) {
        return null;
    }

    @Override
    public CompletionStage<Void> saveAll(Stage.Session session, Collection<Country> objs) {
        return null;
    }

    @Override
    public CompletionStage<Void> deleteAll(Stage.Session session, List<Country> objs) {
        return null;
    }

    @Override
    public CompletionStage<Void> delete(Stage.Session session, Country obj) {
        return null;
    }

    @Override
    public CompletionStage<Integer> deleteAllByIds(Stage.Session session, List<Long> longs) {
        return null;
    }

    @Override
    public CompletionStage<List<Country>> findAllByNames(Stage.Session session, List<String> names) {
        return session.createNamedQuery("Country.findAllByNames", Country.class)
                .setParameter("names", names)
                .getResultList();
    }
}
