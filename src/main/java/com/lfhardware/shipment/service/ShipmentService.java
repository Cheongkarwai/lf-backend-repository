package com.lfhardware.shipment.service;

import com.lfhardware.shipment.client.EasyParcelClient;
import com.lfhardware.shipment.dto.RateCheckingDTO;
import com.lfhardware.shipment.dto.RateCheckingInput;
import com.lfhardware.shipment.dto.order.OrderDTO;
import com.lfhardware.shipment.dto.order.OrderInput;
import com.lfhardware.shipment.dto.order.OrderResultDTO;
import com.lfhardware.shipment.repository.IShipmentRepository;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ShipmentService implements IShipmentService{

    private final IShipmentRepository shipmentRepository;

    private final EasyParcelClient easyParcelClient;


    public ShipmentService(IShipmentRepository shipmentRepository, EasyParcelClient easyParcelClient){
        this.shipmentRepository = shipmentRepository;
        this.easyParcelClient = easyParcelClient;
    }

    public void createShipment(){

    }

    @Override
    public Mono<RateCheckingDTO> findAllRates(RateCheckingInput rateCheckingInput) {
        return easyParcelClient.rateChecking().checkRate(rateCheckingInput)
                .flatMap(Mono::just);
    }

    @PreAuthorize("isFullyAuthenticated()")
    @Override
    public Mono<OrderResultDTO> create(OrderInput orderInput) {
        return easyParcelClient.order().createOrder(orderInput);
    }
}
