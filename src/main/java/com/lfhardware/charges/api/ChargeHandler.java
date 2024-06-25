package com.lfhardware.charges.api;

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
