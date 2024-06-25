package com.lfhardware.shipment.service;

import com.lfhardware.shipment.dto.RateCheckingDTO;
import com.lfhardware.shipment.dto.RateCheckingInput;
import com.lfhardware.shipment.dto.order.OrderDTO;
import com.lfhardware.shipment.dto.order.OrderInput;
import com.lfhardware.shipment.dto.order.OrderResultDTO;
import reactor.core.publisher.Mono;

public interface IShipmentService {

    Mono<RateCheckingDTO> findAllRates(RateCheckingInput rateCheckingInput);

    Mono<OrderResultDTO> create(OrderInput orderInput);

}
