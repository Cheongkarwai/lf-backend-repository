package com.lfhardware.shipment.repository;

import com.lfhardware.shipment.domain.ShippingDetails;
import org.hibernate.reactive.stage.Stage;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletionStage;

@Repository
public class ShipmentRepository implements IShipmentRepository{

    private final Stage.SessionFactory sessionFactory;

    public ShipmentRepository(Stage.SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    @Override
    public CompletionStage<List<ShippingDetails>> findAll(Stage.Session session) {
        return null;
    }

    @Override
    public CompletionStage<ShippingDetails> findById(Stage.Session session, Long aLong) {
        return null;
    }

    @Override
    public CompletionStage<Void> save(Stage.Session session, ShippingDetails obj) {
        return null;
    }

    @Override
    public CompletionStage<List<ShippingDetails>> findAllByIds(Stage.Session session, List<Long> longs) {
        return null;
    }

    @Override
    public CompletionStage<ShippingDetails> merge(Stage.Session session, ShippingDetails obj) {
        return null;
    }

    @Override
    public CompletionStage<Void> deleteById(Stage.Session session, Long aLong) {
        return null;
    }

    @Override
    public ShippingDetails loadReferenceById(Stage.Session session, Long aLong) {
        return null;
    }

    @Override
    public CompletionStage<Void> saveAll(Stage.Session session, Collection<ShippingDetails> objs) {
        return null;
    }

    @Override
    public CompletionStage<Void> deleteAll(Stage.Session session, List<ShippingDetails> objs) {
        return null;
    }

    @Override
    public CompletionStage<Void> delete(Stage.Session session, ShippingDetails obj) {
        return null;
    }

    @Override
    public CompletionStage<Integer> deleteAllByIds(Stage.Session session, List<Long> longs) {
        return null;
    }
}
