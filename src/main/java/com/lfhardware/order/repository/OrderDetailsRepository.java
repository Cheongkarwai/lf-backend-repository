package com.lfhardware.order.repository;

import com.lfhardware.order.domain.OrderDetails;
import org.hibernate.reactive.stage.Stage;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletionStage;

@Repository
public class OrderDetailsRepository implements IOrderDetailsRepository{


    @Override
    public CompletionStage<List<OrderDetails>> findAll(Stage.Session session) {
        return null;
    }

    @Override
    public CompletionStage<OrderDetails> findById(Stage.Session session, Long aLong) {
        return null;
    }

    @Override
    public CompletionStage<Void> save(Stage.Session session, OrderDetails obj) {
        return null;
    }

    @Override
    public CompletionStage<List<OrderDetails>> findAllByIds(Stage.Session session, List<Long> longs) {
        return null;
    }

    @Override
    public CompletionStage<OrderDetails> merge(Stage.Session session, OrderDetails obj) {
        return null;
    }

    @Override
    public CompletionStage<Void> deleteById(Stage.Session session, Long aLong) {
        return null;
    }


    @Override
    public OrderDetails loadReferenceById(Stage.Session session, Long id) {
        return session.getReference(OrderDetails.class,id);
    }

    @Override
    public CompletionStage<Void> saveAll(Stage.Session session, Collection<OrderDetails> orderDetails) {
        return session.persist(orderDetails.toArray(OrderDetails[]::new));
    }

    @Override
    public CompletionStage<Void> deleteAll(Stage.Session session, List<OrderDetails> objs) {
        return null;
    }

    @Override
    public CompletionStage<Void> delete(Stage.Session session, OrderDetails obj) {
        return null;
    }

    @Override
    public CompletionStage<Integer> deleteAllByIds(Stage.Session session, List<Long> longs) {
        return null;
    }
}
