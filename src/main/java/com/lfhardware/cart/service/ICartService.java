package com.lfhardware.cart.service;

import com.lfhardware.cart.dto.CartDTO;
import com.lfhardware.cart.dto.CartItemDTO;
import com.stripe.model.Invoice;
import com.stripe.model.checkout.Session;
import reactor.core.publisher.Mono;

public interface ICartService {

     Mono<Void> addItemToCart(CartItemDTO cartItem);

    Mono<CartDTO> findCart();

    Mono<Void> deleteCartItem(Long stockId, Long cartId);

    Mono<Void> updateCartItemQuantity(Long stockId, Long cartId, CartItemDTO cartItem);

}
