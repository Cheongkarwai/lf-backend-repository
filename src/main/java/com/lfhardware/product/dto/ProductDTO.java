package com.lfhardware.product.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.lfhardware.file.dto.ImageDTO;
import com.lfhardware.review.dto.ReviewDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public record ProductDTO(
        String id, String name, String description, BigDecimal price, CategoryDTO category, BrandDTO brand, Set<StockDTO> stocks
, @JsonProperty("product_images") Set<ImageDTO> productImages, Set<ReviewDTO> reviews){}
