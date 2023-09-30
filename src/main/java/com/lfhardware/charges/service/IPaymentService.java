package com.lfhardware.charges.service;

import co.omise.ClientException;
import co.omise.models.OmiseException;
import co.omise.models.SourceType;
import com.lfhardware.charges.dto.BankCode;
import com.lfhardware.charges.dto.ChargeResponse;
import com.lfhardware.charges.dto.Order;
import com.lfhardware.shared.PaymentMethod;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;

public interface IPaymentService {

    Mono<ChargeResponse> charge(PaymentMethod paymentMethod,String bankCode, Order order) throws ClientException, IOException, OmiseException;

    Mono<List<BankCode>> findAllAvailableBank(SourceType sourceType) ;
}
