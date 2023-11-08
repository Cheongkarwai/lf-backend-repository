package com.lfhardware.cart.handler;

import com.lfhardware.cart.domain.Cart;
import com.lfhardware.cart.dto.CartItem;
import com.lfhardware.cart.service.CartService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class CartHandler {

    private final CartService cartService;


    public CartHandler(CartService cartService){
        this.cartService = cartService;
    }

    public Mono<ServerResponse> addCartItem(ServerRequest serverRequest){
        return serverRequest.bodyToMono(CartItem.class)
                .flatMap(cartItem->this.cartService.addItemToCart(serverRequest.pathVariable("username"),cartItem))
                .flatMap(body->ServerResponse.noContent().build());
    }
}
