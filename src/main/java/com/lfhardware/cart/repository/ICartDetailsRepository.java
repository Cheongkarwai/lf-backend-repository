package com.lfhardware.cart.repository;


import com.lfhardware.cart.domain.CartDetails;
import com.lfhardware.cart.domain.CartDetailsId;
import com.lfhardware.shared.CrudRepository;
import org.hibernate.reactive.stage.Stage;

import java.util.List;
import java.util.concurrent.CompletionStage;

public interface ICartDetailsRepository extends CrudRepository<CartDetails, CartDetailsId> {

    CompletionStage<Integer> updateCartDetailsQuantityById(Stage.Session session, int quantity, CartDetailsId cartDetailsId);
}
