package com.lfhardware.product.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.lfhardware.file.dto.ImageDTO;
import com.lfhardware.review.dto.ReviewDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;


@Data
public class ProductDTO {

    private UUID id;

    private String name;

    private String description;

    private BigDecimal price;

    private CategoryDTO category;

    private BrandDTO brand;

    private Set<StockDTO> stocks;

    @JsonProperty("product_images")
    private Set<ImageDTO> productImages;

    private Set<ReviewDTO> reviews;
}
