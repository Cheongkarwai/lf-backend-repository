package com.lfhardware.charges.api;

import co.omise.ClientException;
import co.omise.models.OmiseException;
import co.omise.models.SourceType;
import com.lfhardware.charges.dto.*;
import com.lfhardware.charges.service.EventService;
import com.lfhardware.charges.service.IPaymentService;
import com.lfhardware.shared.PaymentMethod;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public class PaymentHandler {

    private final IPaymentService paymentService;

    @Value("${stripe.endpoint-secret}")
    private String stripeEndpointSecret;

    private final EventService eventService;



    public PaymentHandler(IPaymentService paymentService, EventService eventService) {
        this.paymentService = paymentService;
        this.eventService = eventService;
    }

    /**
     * @param serverRequest - request object
     * @return Mono<ServerResponse>
     *
     */
    public Mono<ServerResponse> charge(ServerRequest serverRequest) {
        return ServerResponse.ok().body(serverRequest.bodyToMono(Order.class)
                .flatMap(e -> {
                    try {
                        return paymentService.charge(PaymentMethod.valueOf(serverRequest.queryParam("paymentMethod").orElse("TNG")), serverRequest.queryParam("bankCode").orElse(null), e);
                    } catch (ClientException | IOException | OmiseException ex) {
                        return Mono.error(new RuntimeException(ex));
                    }
                }), ChargeResponse.class);
    }

    /**
     * @param serverRequest - request object
     * @return Mono<ServerResponse>
     *
     */
    public Mono<ServerResponse> findAllPaymentMethods(ServerRequest serverRequest) {
        return ServerResponse.ok().body(Mono.just(PaymentMethod.values()), PaymentMethod.class);
    }

    /**
     * @param serverRequest - request object
     * @return Mono<ServerResponse>
     *
     */
    public Mono<ServerResponse> findAllAvailableBank(ServerRequest serverRequest) {
        return ServerResponse.ok().body(paymentService.findAllAvailableBank(SourceType.valueOf(serverRequest.pathVariable("paymentMethod"))), List.class);
    }

    /**
     * @param serverRequest - request object
     * @return Mono<ServerResponse>
     *
     */
    public Mono<ServerResponse> createPaymentIntent(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(PaymentIntentInput.class)
                .flatMap(paymentService::createPaymentIntent)
                .flatMap(e -> ServerResponse.ok().bodyValue(e));
    }

    /**
     * @param serverRequest - request object
     * @return Mono<ServerResponse>
     *
     */
    public Mono<ServerResponse> addPaymentIntentMetadata(ServerRequest serverRequest){
        String id = serverRequest.pathVariable("id");
        return serverRequest.bodyToMono(new ParameterizedTypeReference<Map<String,String>>() {})
                .flatMap(body->paymentService.addPaymentIntentMetadata(id,body))
                .then(Mono.defer(() -> ServerResponse.ok().build()));
    }

    /**
     * @param serverRequest - request object
     * @return Mono<ServerResponse>
     *
     */
    public Mono<ServerResponse> createCheckoutSession(ServerRequest serverRequest) {
        return paymentService.createCheckoutSession()
                .flatMap(e -> ServerResponse.ok().bodyValue(e));
    }


    /**
     * @param serverRequest - request object
     * @return Mono<ServerResponse>
     *
     */
    public Mono<ServerResponse> stripeWebhook(ServerRequest serverRequest) {
        String signatureHeader = serverRequest.headers().firstHeader("Stripe-Signature");
        // Deserialize the nested object inside the event
        //                    EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        //                    StripeObject stripeObject = null;
        //                    if (dataObjectDeserializer.getObject().isPresent()) {
        //                        stripeObject = dataObjectDeserializer.getObject().get();
        //                        System.out.println(stripeObject);
        //                    } else {
        //                        // Deserialization failed, probably due to an API version mismatch.
        //                        // Refer to the Javadoc documentation on `EventDataObjectDeserializer` for
        //                        // instructions on how to handle this case, or return an error here.
        //                    }
        //                    // Handle the event
        //                    System.out.println(event.getType());
        return serverRequest.bodyToMono(String.class)
                .flatMap(body -> {

                    System.out.println("Event");
                    Event event = null;
                    try {
                        System.out.println(body);
                        event = Webhook.constructEvent(body, signatureHeader, stripeEndpointSecret);
                    } catch (SignatureVerificationException e) {
                        return Mono.error(e);
                    }
                    return Mono.just(event);
                }).flatMap(eventService::handleWebhookEvent)
                .then(Mono.defer(() -> ServerResponse.ok().build()));

    }



}
