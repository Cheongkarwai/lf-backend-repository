package com.lfhardware.cart.api;

import com.lfhardware.cart.dto.CartDTO;
import com.lfhardware.cart.dto.CartItemDTO;
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

    public Mono<ServerResponse> saveCartItem(ServerRequest serverRequest){
        return serverRequest.bodyToMono(CartItemDTO.class)
                            .flatMap(cartItem->this.cartService.addItemToCart(cartItem))
                            .flatMap(body->ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> updateCartItemQuantity(ServerRequest serverRequest){

        Long stockId = Long.valueOf(serverRequest.pathVariable("stockId"));
        Long cartId = Long.valueOf(serverRequest.pathVariable("cartId"));

        return serverRequest.bodyToMono(CartItemDTO.class)
                .flatMap(cartItem->this.cartService.updateCartItemQuantity(stockId, cartId, cartItem))
                .flatMap(body->ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> findCart(ServerRequest serverRequest){
        return ServerResponse.ok().body(cartService.findCart(), CartDTO.class);
    }

    public Mono<ServerResponse> deleteCartItem(ServerRequest serverRequest){

        Long stockId = Long.valueOf(serverRequest.pathVariable("stockId"));
        Long cartId = Long.valueOf(serverRequest.pathVariable("cartId"));
        return ServerResponse.noContent().build(cartService.deleteCartItem(stockId,cartId));
    }

}
