package com.lfhardware.product.dto;

import com.lfhardware.file.dto.ImageDTO;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class ProductInput {

    @NotNull(message = "{product-name.non-null}")
    @Length(min = 3)
    private String name;

    @NotNull(message = "{product-description.non-null}")
    private String description;

    @NotNull(message = "{product-price.non-null}")
    @PositiveOrZero
    private BigDecimal price;

    private BrandDTO brand;

    private CategoryDTO category;

    private List<StockInput> stocks = new ArrayList<>();

    private List<ImageDTO> images = new ArrayList<>();

//    @JsonProperty("product_image")
//    private Part productImage;
//
//    @JsonProperty("product_details_images")
//    private Part productDetailsImages;
}
