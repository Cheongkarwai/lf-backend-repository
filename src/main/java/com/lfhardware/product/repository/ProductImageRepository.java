package com.lfhardware.product.repository;

import com.lfhardware.product.domain.ProductImage;
import org.hibernate.reactive.stage.Stage;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletionStage;

@Repository
public class ProductImageRepository implements IProductImageRepository {
    @Override
    public CompletionStage<List<ProductImage>> findAll(Stage.Session session) {
        return null;
    }

    @Override
    public CompletionStage<ProductImage> findById(Stage.Session session, String s) {
        return null;
    }

    @Override
    public CompletionStage<Void> save(Stage.Session session, ProductImage obj) {
        return null;
    }

    @Override
    public CompletionStage<List<ProductImage>> findAllByIds(Stage.Session session, List<String> strings) {
        return null;
    }

    @Override
    public CompletionStage<ProductImage> merge(Stage.Session session, ProductImage obj) {
        return null;
    }

    @Override
    public CompletionStage<Void> deleteById(Stage.Session session, String s) {
        return null;
    }

    @Override
    public ProductImage loadReferenceById(Stage.Session session, String s) {
        return null;
    }

    @Override
    public CompletionStage<Void> saveAll(Stage.Session session, Collection<ProductImage> objs) {
        return null;
    }

    @Override
    public CompletionStage<Void> deleteAll(Stage.Session session, List<ProductImage> objs) {
        return null;
    }

    @Override
    public CompletionStage<Void> delete(Stage.Session session, ProductImage obj) {
        return null;
    }

    @Override
    public CompletionStage<Integer> deleteAllByIds(Stage.Session session, List<String> strings) {
        return null;
    }
}
