package com.lfhardware.product.query.query;

import com.lfhardware.core.repository.Search;
import com.lfhardware.product.dto.ProductFilterCriteria;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductQueryCountAll {

    private Search searchCriteria = new Search();

    private ProductFilterCriteria productFilterCriteria = new ProductFilterCriteria();
}
