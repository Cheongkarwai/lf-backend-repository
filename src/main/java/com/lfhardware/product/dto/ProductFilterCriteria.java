package com.lfhardware.product.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class ProductFilterCriteria {

    private Set<Long> categoryIds = new HashSet<>();

    private Set<Long> brandIds = new HashSet<>();

    @Min(1)
    private Integer quantity;
}
