package com.lfhardware.product.mapper;

import com.lfhardware.file.dto.ImageDTO;
import com.lfhardware.product.domain.ProductImage;
import com.lfhardware.product.dto.ProductForm;
import com.lfhardware.product.domain.Brand;
import com.lfhardware.product.domain.Category;
import com.lfhardware.product.domain.Product;
import com.lfhardware.product.dto.*;
import com.lfhardware.review.mapper.ReviewMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Mappings;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {StockMapper.class, ReviewMapper.class})
public interface ProductMapper {

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "name", target = "name"),
            @Mapping(source = "description", target = "description"),
            @Mapping(source =  "price",target = "price"),
            @Mapping(source = "brand.id", target = "brand.id"),
            @Mapping(source = "brand.name", target = "brand.name"),
            @Mapping(source = "category.id", target = "category.id"),
            @Mapping(source = "category.name", target = "category.name"),
            @Mapping(source = "stocks",target = "stocks"),
            @Mapping(source = "productImages", target = "productImages"),
            @Mapping(source = "reviews", target = "reviews")
    })
    ProductDTO mapToProductDTO(Product product);

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "name", target = "name"),
    })
    CategoryDTO mapToCategoryDTO(Category category);

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "name", target = "name"),
    })
    BrandDTO mapToBrandDTO(Brand brand);

    @Mappings({
            @Mapping(source = "name", target = "name"),
            @Mapping(source = "description", target = "description"),
            @Mapping(source =  "price",target = "price"),
            @Mapping(source = "brand.id", target = "brand.id"),
            @Mapping(source = "brand.name", target = "brand.name"),
            @Mapping(source = "category.id", target = "category.id"),
            @Mapping(source = "category.name", target = "category.name"),
    })
    Product mapToProduct(ProductInput productInput);

    @Mappings({
            @Mapping(source = "name", target = "name"),
            @Mapping(source = "description", target = "description"),
            @Mapping(source =  "price",target = "price"),
            @Mapping(source = "brand.id", target = "brand.id"),
            @Mapping(source = "brand.name", target = "brand.name"),
            @Mapping(source = "category.id", target = "category.id"),
            @Mapping(source = "category.name", target = "category.name"),
    })
    Product mapToProduct(ProductForm productForm);


    @Mappings({
            @Mapping(target = "url", source = "path"),
            @Mapping(target = "type", source = "type"),
    })
    ProductImage mapToProductImage(ImageDTO imageDTO);

    @Mappings({
            @Mapping(source = "url",target = "path"),
            @Mapping(target = "type", source = "type")
    })
    ImageDTO mapToProductImage(ProductImage productImage);
}
