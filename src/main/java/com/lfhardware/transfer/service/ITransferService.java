package com.lfhardware.transfer.service;

import reactor.core.publisher.Mono;

public interface ITransferService {

    Mono<Void> transferPayment();
}
