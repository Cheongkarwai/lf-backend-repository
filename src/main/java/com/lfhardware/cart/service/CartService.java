package com.lfhardware.cart.service;

import com.lfhardware.cart.domain.Cart;
import com.lfhardware.cart.dto.CartItem;
//import com.lfhardware.cart.repository.CartItemRepository;
//import com.lfhardware.cart.repository.CartRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CartService {

//    private final CartItemRepository cartItemRepository;
//
//    private final CartRepository cartRepository;
//
//    public CartService(CartItemRepository cartItemRepository, CartRepository cartRepository){
//        this.cartItemRepository = cartItemRepository;
//        this.cartRepository = cartRepository;
//    }

    public Mono<com.lfhardware.cart.domain.CartItem> addItemToCart(String username, CartItem cartItem){
//        return cartRepository.save(Cart.builder().username(username).build())
//                .flatMap(e -> cartItemRepository.save(com.lfhardware.cart.domain.CartItem.builder().productId(e.getId()).cartId(e.getId()).quantity(cartItem.getQuantity()).build()));
return Mono.empty();
    }
}
