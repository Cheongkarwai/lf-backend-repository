package com.lfhardware.charges.dto;

import com.lfhardware.shared.Currency;

import java.math.BigDecimal;
import java.util.Map;

public record PaymentIntentInput(BigDecimal amount, Currency currency, Map<String,String> metadata) { }
