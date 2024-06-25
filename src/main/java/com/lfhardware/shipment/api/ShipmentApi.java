package com.lfhardware.shipment.api;

import com.lfhardware.shipment.dto.RateCheckingInput;
import com.lfhardware.shipment.dto.order.OrderInput;
import com.lfhardware.shipment.dto.order.OrderResultDTO;
import com.lfhardware.shipment.service.IShipmentService;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class ShipmentApi {

    private final IShipmentService shipmentService;

    public ShipmentApi(IShipmentService shipmentService) {
        this.shipmentService = shipmentService;
    }

    public Mono<ServerResponse> createShipment(ServerRequest serverRequest) {
        return null;
    }

    public Mono<ServerResponse> getAllAvailableShipments(ServerRequest serverRequest){
        return serverRequest.bodyToMono(RateCheckingInput.class)
                .flatMap(shipmentService::findAllRates)
                .flatMap(rateCheckingDTO->ServerResponse.ok().bodyValue(rateCheckingDTO))
                .onErrorResume(e-> ServerResponse.status(HttpStatusCode.valueOf(503)).build());
    }

    public Mono<ServerResponse> createOrder(ServerRequest serverRequest){
        return serverRequest.bodyToMono(OrderInput.class)
                .flatMap(shipmentService::create)
                .flatMap(e-> ServerResponse.ok().bodyValue(e));
                //.onErrorResume(error-> ServerResponse.badRequest().build());
    }
}
