package com.lfhardware.charges.handler;

import co.omise.ClientException;
import co.omise.models.OmiseException;
import co.omise.models.SourceType;
import com.lfhardware.charges.dto.ChargeResponse;
import com.lfhardware.charges.dto.Order;
import com.lfhardware.charges.service.IPaymentService;
import com.lfhardware.shared.PaymentMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.io.IOException;

//@Component
//public class ChargeHandler {
//
//    private final IPaymentService paymentService;
//
//    public ChargeHandler(IPaymentService paymentService){
//        this.paymentService = paymentService;
//    }
//
//    public Mono<ServerResponse> charge(ServerRequest serverRequest){
//        return ServerResponse.ok().body(serverRequest.bodyToMono(Order.class)
//                .flatMap(e-> {
//                    try {
//                        return paymentService.charge(PaymentMethod.valueOf(serverRequest.queryParam("paymentMethod").orElse("TNG")),e);
//                    } catch (ClientException | IOException | OmiseException ex) {
//                        return Mono.error(new RuntimeException(ex));
//                    }
//                }), ChargeResponse.class);
//    }
//}
