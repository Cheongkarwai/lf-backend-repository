package com.lfhardware.product.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.lfhardware.file.dto.ImageDTO;
import com.lfhardware.review.dto.ReviewDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDTO {

    private UUID id;

    private String name;

    private String description;

    private BigDecimal price;

    private CategoryDTO category;

    private BrandDTO brand;

    private Set<StockDTO> stocks = new HashSet<>();

    private Set<ImageDTO> productImages = new HashSet<>();

    private Set<ReviewDTO> reviews = new HashSet<>();

    private OffsetDateTime createdAt;

}
