package com.lfhardware.charges.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Item {

    private String name;

    private String category;

    private BigDecimal amount;

    private String sku;
}
