package com.lfhardware.cart.repository;

import com.lfhardware.cart.domain.Cart;
import org.hibernate.reactive.stage.Stage;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletionStage;

@Repository
public class CartRepository implements ICartRepository{

    private final Stage.SessionFactory sessionFactory;

    public CartRepository(Stage.SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }
    @Override
    public CompletionStage<List<Cart>> findAll(Stage.Session session) {
        return null;
    }

    @Override
    public CompletionStage<Cart> findById(Stage.Session session, Long aLong) {
        return null;
    }

    @Override
    public CompletionStage<Void> save(Stage.Session session, Cart obj) {
        return session.persist(obj);
    }

    @Override
    public CompletionStage<List<Cart>> findAllByIds(Stage.Session session, List<Long> longs) {
        return null;
    }

    @Override
    public CompletionStage<Cart> merge(Stage.Session session, Cart obj) {
        return null;
    }

    @Override
    public CompletionStage<Void> deleteById(Stage.Session session, Long aLong) {
        return null;
    }

    @Override
    public Cart loadReferenceById(Stage.Session session, Long aLong) {
        return null;
    }

    @Override
    public CompletionStage<Void> saveAll(Stage.Session session, Collection<Cart> objs) {
        return null;
    }

    @Override
    public CompletionStage<Void> deleteAll(Stage.Session session, List<Cart> objs) {
        return null;
    }

    @Override
    public CompletionStage<Void> delete(Stage.Session session, Cart obj) {
        return null;
    }

    @Override
    public CompletionStage<Integer> deleteAllByIds(Stage.Session session, List<Long> longs) {
        return null;
    }

    @Override
    public CompletionStage<Cart> findByUsername(Stage.Session session, String username) {
        return session.createNamedQuery("Cart.findByUsername",Cart.class)
                .setParameter("username",username)
                .getSingleResultOrNull();
    }

    @Override
    public CompletionStage<Cart> findAllCartStocksByUsername(Stage.Session session, String username) {
        return session.createNamedQuery("Cart.findByUsername",Cart.class)
                .setParameter("username",username)
                .setPlan(session.getEntityGraph(Cart.class,"CartItem"))
                .getSingleResultOrNull();
    }
}
