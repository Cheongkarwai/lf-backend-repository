package com.lfhardware.sales.util;

import com.lfhardware.shared.Currency;

import java.math.BigDecimal;

public class CurrencySmallestUnitConverter {

    public static BigDecimal convert(Currency currency, BigDecimal amount){
        return switch (currency) {
            case MYR-> amount.multiply(BigDecimal.valueOf(100));
        };
    }
}
