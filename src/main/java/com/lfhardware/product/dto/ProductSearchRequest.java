package com.lfhardware.product.dto;

import com.lfhardware.core.repository.Search;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductSearchRequest {

    @Valid
    private ProductFilterCriteria filter;

    private Search search;
}
