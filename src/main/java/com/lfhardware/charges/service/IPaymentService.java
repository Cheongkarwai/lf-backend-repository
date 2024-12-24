package com.lfhardware.charges.service;

import co.omise.ClientException;
import co.omise.models.OmiseException;
import co.omise.models.SourceType;
import com.lfhardware.charges.dto.*;

import com.lfhardware.core.dto.Currency;
import com.lfhardware.core.dto.PaymentMethod;
import com.stripe.model.Charge;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IPaymentService {

    Mono<ChargeResponse> charge(PaymentMethod paymentMethod,String bankCode, Order order) throws ClientException, IOException, OmiseException;

    Mono<List<BankCode>> findAllAvailableBank(SourceType sourceType);


    Mono<PaymentIntentDTO> createPaymentIntent(PaymentIntentInput paymentIntentInput);

    Mono<String> createCheckoutSession();

    Mono<Void> addPaymentIntentMetadata(String id, Map<String, String> metadata);

    Mono<Void> transferFunds(Currency currency, Long amount, String transferGroup, String connectedAccountId);

    Mono<Charge> findChargeByPaymentIntent(String paymentIntentId);



}
