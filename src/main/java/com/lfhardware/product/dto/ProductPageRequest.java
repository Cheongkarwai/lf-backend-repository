package com.lfhardware.product.dto;

import com.lfhardware.core.dto.PageInfo;
import com.lfhardware.core.repository.Search;
import com.lfhardware.core.repository.Sort;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductPageRequest extends PageInfo {

    private Set<Long> categoryIds = new HashSet<>();

    private Set<Long> brandIds = new HashSet<>();

    private Integer minQuantity;


    public ProductPageRequest(int pageSize, int page, Sort sort, Search search, Set<Long> categoryIds, Set<Long> brandIds, Integer minQuantity) {
        super(pageSize, page, sort, search);
        this.categoryIds = categoryIds;
        this.brandIds = brandIds;
        this.minQuantity = minQuantity;
    }

    public ProductPageRequest(int pageSize, int page, Sort sort, Search search){
        super(pageSize, page, sort, search);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ProductPageRequest that = (ProductPageRequest) o;
        return Objects.equals(categoryIds, that.categoryIds) && Objects.equals(brandIds, that.brandIds) && Objects.equals(minQuantity, that.minQuantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), categoryIds, brandIds,minQuantity);
    }
}
