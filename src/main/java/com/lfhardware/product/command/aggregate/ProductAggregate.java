package com.lfhardware.product.command.aggregate;

import com.lfhardware.product.command.command.CreateProductCommand;
import com.lfhardware.product.command.command.DeleteProductCommand;
import com.lfhardware.product.command.command.UpdateProductCommand;
import com.lfhardware.product.event.ProductCreatedEvent;
import com.lfhardware.product.event.ProductDeleteEvent;
import com.lfhardware.product.event.ProductUpdateEvent;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

import java.math.BigDecimal;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Slf4j
@Data
@NoArgsConstructor
@Aggregate
public class ProductAggregate {

    @AggregateIdentifier
    private String id;

    private String name;

    private String description;

    private BigDecimal price;

    @CommandHandler
    public ProductAggregate(CreateProductCommand createProductCommand){
        apply(new ProductCreatedEvent(createProductCommand.getId(),
                createProductCommand.getName(),
                createProductCommand.getDescription(),
                createProductCommand.getPrice(), null));
    }

    @CommandHandler
    public void handle(UpdateProductCommand updateProductCommand){
        apply(new ProductUpdateEvent(updateProductCommand.getId(),
                updateProductCommand.getName(),
                updateProductCommand.getDescription(),
                updateProductCommand.getPrice()));
    }

    @CommandHandler
    public void handle(DeleteProductCommand deleteProductCommand){
        apply(new ProductDeleteEvent(deleteProductCommand.getId()));
    }

    @EventSourcingHandler
    public void on(ProductCreatedEvent productCreatedEvent){
        log.info("Handle event sourcing. ProductCreatedEvent={}", productCreatedEvent);
        this.id = productCreatedEvent.getId();
        this. name = productCreatedEvent.getName();
        this.description = productCreatedEvent.getDescription();
        this.price = productCreatedEvent.getPrice();

    }

    @EventSourcingHandler
    public void on(ProductUpdateEvent productUpdateEvent){
        log.info("Handle event sourcing. ProductUpdateEvent={}", productUpdateEvent);
        this.id = productUpdateEvent.getId();
        this. name = productUpdateEvent.getName();
        this.description = productUpdateEvent.getDescription();
        this.price = productUpdateEvent.getPrice();

    }

    @CommandHandler
    public void on(ProductDeleteEvent productDeleteEvent){
        log.info("Handle event sourcing. ProductDeleteEvent={}", productDeleteEvent);
        this.id = productDeleteEvent.getId();
    }

}
