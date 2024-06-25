package com.lfhardware.invoice.service;

import com.lfhardware.invoice.dto.InvoiceInput;
import reactor.core.publisher.Mono;

public interface InvoiceService {

    Mono<Void> createInvoice(InvoiceInput invoiceInput);
}
