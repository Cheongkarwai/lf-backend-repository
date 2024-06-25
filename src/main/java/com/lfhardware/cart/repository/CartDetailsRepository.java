package com.lfhardware.cart.repository;

import com.lfhardware.cart.domain.CartDetails;
import com.lfhardware.cart.domain.CartDetailsId;
import com.lfhardware.cart.domain.CartDetails_;
import jakarta.persistence.criteria.*;
import org.hibernate.reactive.stage.Stage;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletionStage;

@Repository
public class CartDetailsRepository implements ICartDetailsRepository{

    private final Stage.SessionFactory sessionFactory;

    public CartDetailsRepository(Stage.SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }
    @Override
    public CompletionStage<List<CartDetails>> findAll(Stage.Session session) {
        return null;
    }

    @Override
    public CompletionStage<CartDetails> findById(Stage.Session session, CartDetailsId cartDetailsId) {
        return session.find(CartDetails.class,cartDetailsId);
    }

    @Override
    public CompletionStage<Void> save(Stage.Session session, CartDetails obj) {
        return session.persist(obj);
    }

    @Override
    public CompletionStage<List<CartDetails>> findAllByIds(Stage.Session session, List<CartDetailsId> cartDetailsIds) {
        return null;
    }

    @Override
    public CompletionStage<CartDetails> merge(Stage.Session session, CartDetails obj) {
        return session.merge(obj);
    }

    @Override
    public CompletionStage<Void> deleteById(Stage.Session session, CartDetailsId cartDetailsId) {
        CartDetails cartDetails = session.getReference(CartDetails.class,cartDetailsId);
        return session.remove(cartDetails);
    }


    @Override
    public CartDetails loadReferenceById(Stage.Session session, CartDetailsId cartDetailsId) {
        return null;
    }

    @Override
    public CompletionStage<Void> saveAll(Stage.Session session, Collection<CartDetails> objs) {
        return null;
    }

    @Override
    public CompletionStage<Integer> updateCartDetailsQuantityById(Stage.Session session, int quantity, CartDetailsId cartDetailsId) {
        CriteriaBuilder cb = sessionFactory.getCriteriaBuilder();
        CriteriaUpdate<CartDetails> criteriaUpdate = cb.createCriteriaUpdate(CartDetails.class);
        Root<CartDetails> root = criteriaUpdate.from(CartDetails.class);
        criteriaUpdate.set(root.get(CartDetails_.QUANTITY),quantity)
                .where(cb.equal(root.get(CartDetails_.CART_DETAILS_ID),cartDetailsId));
        return session.createQuery(criteriaUpdate).executeUpdate();
    }

    @Override
    public CompletionStage<Void> deleteAll(Stage.Session session, List<CartDetails> cartDetails) {
        return session.remove(cartDetails.toArray(CartDetails[]::new));
    }

    @Override
    public CompletionStage<Void> delete(Stage.Session session, CartDetails cartDetails){
        return session.remove(cartDetails);
    }

    @Override
    public CompletionStage<Integer> deleteAllByIds(Stage.Session session, List<CartDetailsId> ids) {
        CriteriaBuilder cb = sessionFactory.getCriteriaBuilder();
        CriteriaDelete<CartDetails> cd = cb.createCriteriaDelete(CartDetails.class);
        Root root = cd.from(CartDetails.class);
        cd.where(root.get(CartDetails_.cartDetailsId).in(ids));
        return session.createQuery(cd).executeUpdate();
    }
}
