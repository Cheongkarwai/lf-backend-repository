package com.lfhardware.checkout.service;

import com.lfhardware.checkout.dto.CheckoutInput;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.checkout.Session;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface ICheckoutService {

//    String createCheckoutSession(CheckoutInput checkoutInput) throws StripeException;
    Mono<Session> createCheckoutSession(String transferGroup, CheckoutInput checkoutInput);

    Mono<Session> findById(String id);


}
