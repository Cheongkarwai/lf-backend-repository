package com.lfhardware.transfer.service;

import com.stripe.StripeClient;
import com.stripe.model.Transfer;
import com.stripe.param.TransferCreateParams;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class TransferService implements ITransferService{

    private StripeClient stripeClient;

    public TransferService(StripeClient stripeClient){
        this.stripeClient = stripeClient;
    }

    public Mono<Void> transferPayment(){
        return Mono.fromCallable(()->{
            TransferCreateParams params =
                    TransferCreateParams.builder()
                            .setAmount(10000L)
                            .setCurrency("MYR")
                            .setDestination("acct_1PBe1RCcojotUMkU")
                            .setTransferGroup("ORDER100")
                            .build();

            Transfer transfer = stripeClient.transfers()
                    .create(params);

            return transfer;
        }).subscribeOn(Schedulers.boundedElastic()).then();
    }
}
