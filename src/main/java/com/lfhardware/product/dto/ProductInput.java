package com.lfhardware.product.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lfhardware.file.dto.ImageDTO;
import com.lfhardware.stock.domain.Size;
import lombok.*;
import org.springframework.http.codec.multipart.Part;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class ProductInput {

    private String name;

    private String description;

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
