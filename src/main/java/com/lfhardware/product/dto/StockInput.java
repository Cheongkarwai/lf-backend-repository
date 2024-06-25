package com.lfhardware.product.dto;

import com.lfhardware.stock.domain.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StockInput {

    private int quantity;

//    private String measurement;

    private Size size;

    private Double length;

    private Double width;

    private Double height;

    private Double weight;
}
