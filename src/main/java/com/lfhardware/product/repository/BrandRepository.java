package com.lfhardware.product.repository;

import com.lfhardware.product.domain.Brand;
import org.hibernate.reactive.stage.Stage;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletionStage;

@Repository
public class BrandRepository implements IBrandRepository{

    private final Stage.SessionFactory sessionFactory;

    public BrandRepository(Stage.SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }
    @Override
    public CompletionStage<List<Brand>> findAll(Stage.Session session) {
        return session.createQuery("SELECT u FROM Brand u",Brand.class)
                .getResultList();
    }

    @Override
    public CompletionStage<Brand> findById(Stage.Session session, Long aLong) {
        return null;
    }

    @Override
    public CompletionStage<Void> save(Stage.Session session, Brand obj) {
        return null;
    }

    @Override
    public CompletionStage<List<Brand>> findAllByIds(Stage.Session session, List<Long> longs) {
        return null;
    }

    @Override
    public CompletionStage<Brand> merge(Stage.Session session, Brand obj) {
        return null;
    }

    @Override
    public CompletionStage<Void> deleteById(Stage.Session session, Long aLong) {
        return null;
    }


    @Override
    public Brand loadReferenceById(Stage.Session session, Long aLong) {
        return null;
    }

    @Override
    public CompletionStage<Void> saveAll(Stage.Session session, Collection<Brand> objs) {
        return null;
    }

    @Override
    public CompletionStage<Void> deleteAll(Stage.Session session, List<Brand> objs) {
        return null;
    }

    @Override
    public CompletionStage<Void> delete(Stage.Session session, Brand obj) {
        return null;
    }

    @Override
    public CompletionStage<Integer> deleteAllByIds(Stage.Session session, List<Long> longs) {
        return null;
    }
}
