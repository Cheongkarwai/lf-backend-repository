package com.lfhardware.product.dto;

import com.lfhardware.product.dto.BrandDTO;
import com.lfhardware.product.dto.CategoryDTO;
import com.lfhardware.product.dto.StockDTO;
import com.lfhardware.product.dto.StockInput;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductForm {

    private String name;

    private String description;

    private BigDecimal price;

    private CategoryDTO category;

    private BrandDTO brand;

    private List<StockInput> stocks;
}
