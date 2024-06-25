package com.lfhardware.stock.repository;

import com.lfhardware.stock.domain.Size;
import com.lfhardware.stock.domain.Stock;
import org.hibernate.reactive.stage.Stage;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletionStage;

@Repository
public class StockRepository implements IStockRepository{
    @Override
    public CompletionStage<List<Stock>> findAll(Stage.Session session) {
        return session.createQuery("SELECT s FROM Stock s",Stock.class)
                .getResultList();
    }

    @Override
    public CompletionStage<Stock> findById(Stage.Session session, Long aLong) {
        return null;
    }

    @Override
    public CompletionStage<Void> save(Stage.Session session, Stock obj) {
        return null;
    }

    @Override
    public CompletionStage<List<Stock>> findAllByIds(Stage.Session session, List<Long> ids) {
        return session.find(Stock.class,ids.toArray(Long[]::new));
    }

    @Override
    public CompletionStage<Stock> merge(Stage.Session session, Stock obj) {
        return null;
    }

    @Override
    public CompletionStage<Void> deleteById(Stage.Session session, Long aLong) {
        return null;
    }


    @Override
    public Stock loadReferenceById(Stage.Session session, Long aLong) {
        return session.getReference(Stock.class,aLong);
    }

    @Override
    public CompletionStage<Void> saveAll(Stage.Session session, Collection<Stock> objs) {
        return session.persist(objs.toArray(Stock[]::new));
    }

    @Override
    public CompletionStage<Void> deleteAll(Stage.Session session, List<Stock> objs) {
        return null;
    }

    @Override
    public CompletionStage<Void> delete(Stage.Session session, Stock obj) {
        return null;
    }

    @Override
    public CompletionStage<Integer> deleteAllByIds(Stage.Session session, List<Long> longs) {
        return null;
    }

    @Override
    public CompletionStage<Stock> findByProductIdAndSize(Stage.Session session, Long productId, Size size) {
        return session.createNamedQuery("Stock.findByProductIdAndSize",Stock.class)
                .setParameter("id",productId)
                .setParameter("size",size)
                .getSingleResult();
    }
}
