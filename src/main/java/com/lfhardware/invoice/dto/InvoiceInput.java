package com.lfhardware.invoice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lfhardware.shared.Currency;

import java.util.List;

public record InvoiceInput(@JsonProperty("stripe_customer_id") String stripeCustomerId, @JsonProperty("stock_ids") List<String> stockIds,
                           @JsonProperty("cart_id") String cartId, Currency currency) {

}
