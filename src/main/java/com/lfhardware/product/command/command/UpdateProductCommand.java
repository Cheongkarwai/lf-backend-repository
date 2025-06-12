package com.lfhardware.product.command.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProductCommand {

    @TargetAggregateIdentifier
    private String id;

    private String name;

    private String description;

    private BigDecimal price;
}
