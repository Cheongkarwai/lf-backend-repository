package com.lfhardware.checkout.service;

import com.lfhardware.checkout.dto.CheckoutInput;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface ICheckoutService {

    Mono<String> createCheckoutSession(CheckoutInput checkoutInput);
}
