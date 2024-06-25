package com.lfhardware.product.repository;

import com.lfhardware.product.domain.Category;
import org.hibernate.reactive.stage.Stage;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletionStage;

@Repository
public class CategoryRepository implements ICategoryRepository{

    private Stage.SessionFactory sessionFactory;

    public CategoryRepository(Stage.SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }


    @Override
    public CompletionStage<List<Category>> findAll(Stage.Session session) {
        return session.createQuery("SELECT u FROM Category u",Category.class)
                .getResultList();
    }

    @Override
    public CompletionStage<Category> findById(Stage.Session session, Long aLong) {
        return null;
    }

    @Override
    public CompletionStage<Void> save(Stage.Session session, Category obj) {
        return null;
    }

    @Override
    public CompletionStage<List<Category>> findAllByIds(Stage.Session session, List<Long> longs) {
        return null;
    }

    @Override
    public CompletionStage<Category> merge(Stage.Session session, Category obj) {
        return null;
    }

    @Override
    public CompletionStage<Void> deleteById(Stage.Session session, Long aLong) {
        return null;
    }


    @Override
    public Category loadReferenceById(Stage.Session session, Long aLong) {
        return null;
    }

    @Override
    public CompletionStage<Void> saveAll(Stage.Session session, Collection<Category> objs) {
        return null;
    }

    @Override
    public CompletionStage<Void> deleteAll(Stage.Session session, List<Category> objs) {
        return null;
    }

    @Override
    public CompletionStage<Void> delete(Stage.Session session, Category obj) {
        return null;
    }

    @Override
    public CompletionStage<Integer> deleteAllByIds(Stage.Session session, List<Long> longs) {
        return null;
    }
}
