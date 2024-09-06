package com.lfhardware.checkout.api;

import com.lfhardware.checkout.dto.CheckoutInput;
import com.lfhardware.checkout.service.ICheckoutService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class CheckoutApi {

    private final ICheckoutService checkoutService;

    public CheckoutApi(ICheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    /**
     * @param serverRequest - request object
     * @return Mono<ServerResponse>
     *
     */
    public Mono<ServerResponse> createCheckoutSession(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(CheckoutInput.class)
                .flatMap(checkoutInput -> checkoutService.createCheckoutSession(null,checkoutInput)
                        .flatMap(checkoutUrl -> ServerResponse.ok()
                                .bodyValue(checkoutUrl)));
    }
}
