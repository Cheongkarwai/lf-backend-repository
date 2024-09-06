package com.lfhardware.transfer.api;

import com.lfhardware.transfer.service.ITransferService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class TransferApi {

    private final ITransferService transferService;

    private TransferApi(ITransferService transferService){
        this.transferService = transferService;
    }

    /**
     * @param serverRequest - request object
     * @return Mono<ServerResponse>
     *
     */
    public Mono<ServerResponse> transferPayment(ServerRequest serverRequest){
        return transferService.transferPayment()
                .then(ServerResponse.ok().build());
    }
}
