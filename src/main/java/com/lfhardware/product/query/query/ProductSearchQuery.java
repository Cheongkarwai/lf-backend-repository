package com.lfhardware.product.query.query;

import com.lfhardware.core.dto.PageRequest;
import com.lfhardware.core.repository.Search;
import com.lfhardware.product.dto.ProductFilterCriteria;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductSearchQuery {

    private PageRequest pageRequest;

    private Search searchCriteria;

    private ProductFilterCriteria productFilterCriteria;
}
