package com.lfhardware.cart.repository;


import com.lfhardware.cart.domain.Cart;
import com.lfhardware.shared.CrudRepository;
import org.hibernate.reactive.stage.Stage;

import java.util.concurrent.CompletionStage;

public interface ICartRepository extends CrudRepository<Cart,Long> {

    CompletionStage<Cart> findByUsername(Stage.Session session, String username);

    CompletionStage<Cart> findAllCartStocksByUsername(Stage.Session session, String username);
}
