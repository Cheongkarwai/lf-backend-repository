package com.lfhardware.product.command.event;

import com.lfhardware.product.domain.Product;
import com.lfhardware.product.event.ProductCreatedEvent;
import com.lfhardware.product.event.ProductDeleteEvent;
import com.lfhardware.product.event.ProductUpdateEvent;
import com.lfhardware.product.mapper.ProductMapper;
import com.lfhardware.product.repository.IProductRepository;
import io.smallrye.mutiny.converters.uni.UniReactorConverters;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.hibernate.reactive.mutiny.Mutiny;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class ProductEventHandler {

    private final ProductMapper productMapper;

    private final IProductRepository productRepository;

    private final Mutiny.SessionFactory sessionFactory;

    public ProductEventHandler(ProductMapper productMapper,
                               IProductRepository productRepository,
                               Mutiny.SessionFactory sessionFactory) {
        this.productMapper = productMapper;
        this.productRepository = productRepository;
        this.sessionFactory = sessionFactory;
    }

    @EventHandler
    public void on(ProductCreatedEvent productCreatedEvent) {
        log.info("Handling ProductCreatedEvent. event={}", productCreatedEvent);
        Product product = productMapper.mapToProduct(productCreatedEvent);
        sessionFactory.withTransaction((session, transaction) ->
                        productRepository.merge(session, product))
                .convert()
                .with(UniReactorConverters.toMono())
                .subscribe();
    }

    @EventHandler
    public void on(ProductUpdateEvent productUpdateEvent) {
        log.info("Handling ProductUpdateEvent. event={}", productUpdateEvent);
        sessionFactory.withTransaction((session, transaction) ->
                        productRepository.findById(session, UUID.fromString(productUpdateEvent.getId()))
                        .map(product -> {
                            productMapper.mapToProduct(productUpdateEvent, product);
                            return product;
                        })
                        .chain(product -> productRepository.save(session, product)))
                .convert()
                .with(UniReactorConverters.toMono())
                .subscribe();
    }

    @EventHandler
    public void on(ProductDeleteEvent productDeleteEvent){
        log.info("Handling ProductDeleteEvent. event={}", productDeleteEvent);
        sessionFactory.withTransaction((session, transaction) ->
                        productRepository.deleteById(session, UUID.fromString(productDeleteEvent.getId())))
                .convert()
                .with(UniReactorConverters.toMono())
                .subscribe();
    }
}
