package com.lfhardware.invoice.service;

import com.lfhardware.cart.dto.ItemDTO;
import com.lfhardware.cart.service.CartService;
import com.lfhardware.cart.service.ICartService;
import com.lfhardware.invoice.dto.InvoiceInput;
import com.lfhardware.sales.util.CurrencySmallestUnitConverter;
import com.lfhardware.shared.Currency;
import com.stripe.StripeClient;
import com.stripe.exception.StripeException;
import com.stripe.model.Invoice;
import com.stripe.model.InvoiceItem;
import com.stripe.model.PaymentIntent;
import com.stripe.param.InvoiceCreateParams;
import com.stripe.param.InvoiceFinalizeInvoiceParams;
import com.stripe.param.InvoiceItemCreateParams;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final StripeClient stripeClient;

    private final ICartService cartService;

    public InvoiceServiceImpl(StripeClient stripeClient, ICartService cartService) {
        this.stripeClient = stripeClient;
        this.cartService = cartService;
    }

    @Override
    public Mono<Void> createInvoice(InvoiceInput invoiceInput) {
        InvoiceCreateParams invoiceCreateParams = new InvoiceCreateParams.Builder()
                .setCurrency(Currency.MYR.toString()).setCustomer(invoiceInput.stripeCustomerId()).build();
        try {
            return Mono.just(stripeClient.invoices().create(invoiceCreateParams))
                    .flatMap(invoice-> createInvoiceItems(invoice.getId(),invoiceInput));
        } catch (StripeException e) {
            return Mono.error(e);
        }
    }

    private Mono<Void> createInvoiceItems(String invoiceId, InvoiceInput invoiceInput) {
        return cartService.findCart().map(cartDTO -> {

                    List<InvoiceItemCreateParams> invoiceItemCreateParamsList = new ArrayList<>();

                    for (ItemDTO itemDTO : cartDTO.getItems()) {
                        InvoiceItemCreateParams invoiceItemCreateParams = new InvoiceItemCreateParams.Builder()
                                .setInvoice(invoiceId)
                                .setDescription(itemDTO.name())
                                .setCurrency(invoiceInput.currency().toString())
                                .setUnitAmountDecimal(CurrencySmallestUnitConverter.convert(invoiceInput.currency(),itemDTO.price()))
                                .setQuantity(Long.valueOf(itemDTO.quantity()))
                                .setCustomer(invoiceInput.stripeCustomerId())
                                .build();

                        invoiceItemCreateParamsList.add(invoiceItemCreateParams);
                    }
                    return invoiceItemCreateParamsList;
                }).flatMapMany(Flux::fromIterable)
                .flatMap(invoiceItemCreateParams -> {
                    try {
                        return Mono.just(stripeClient.invoiceItems().create(invoiceItemCreateParams));
                    } catch (StripeException e) {
                        return Mono.error(e);
                    }
                }).flatMap(e->{
                    Invoice resource = null;
                    try {
                        resource = stripeClient.invoices().retrieve(e.getInvoice());
                    } catch (StripeException ex) {
                        return Mono.error(new RuntimeException(ex));
                    }
                    InvoiceFinalizeInvoiceParams params = InvoiceFinalizeInvoiceParams.builder().build();
                    try {
                        return Mono.just(stripeClient.invoices().finalizeInvoice(e.getInvoice(),params));
                    } catch (StripeException ex) {
                        return Flux.error(new RuntimeException(ex));
                    }
                }).flatMap(finalizedInvoice->{
                    try {
                        PaymentIntent paymentIntent = stripeClient.paymentIntents().retrieve("pi_3OnLSvEBRksaPF5n0huO34PN");
                        finalizedInvoice.pay();
                    } catch (StripeException e) {
                        return Flux.error(new RuntimeException(e));
                    }
                    return Mono.empty();
                }).then();
    }
}
