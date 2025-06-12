package com.lfhardware.product.command.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record UpdateProductInput(@NotBlank(message = "{product.name.not-blank}") String name,
                                 String description,
                                 @NotNull(message = "{product.price.not-null}") @Positive(message = "{product.price.not-positive}") BigDecimal price){}
