package com.lfhardware.invoice.api;

import com.lfhardware.invoice.dto.InvoiceInput;
import com.lfhardware.invoice.service.InvoiceService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class InvoiceHandler {

    private final InvoiceService invoiceService;

    public InvoiceHandler(InvoiceService invoiceService){
        this.invoiceService = invoiceService;
    }

    /**
     * @param serverRequest - request object
     * @return Mono<ServerResponse>
     *
     */
    public Mono<ServerResponse> createInvoice(ServerRequest serverRequest){
        return serverRequest.bodyToMono(InvoiceInput.class)
                .flatMap(invoiceInput -> invoiceService.createInvoice(invoiceInput))
                .flatMap(paymentIntentId->ServerResponse.ok().build());
    }
}
