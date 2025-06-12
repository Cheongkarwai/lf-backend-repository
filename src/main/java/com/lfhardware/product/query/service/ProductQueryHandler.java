package com.lfhardware.product.query.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.lfhardware.product.dto.ProductDTO;
import com.lfhardware.product.exception.ProductNotFoundException;
import com.lfhardware.product.mapper.ProductMapper;
import com.lfhardware.product.query.query.ProductByIdQuery;
import com.lfhardware.product.query.query.ProductQueryCountAll;
import com.lfhardware.product.query.query.ProductSearchQuery;
import com.lfhardware.product.repository.IProductRepository;
import io.smallrye.mutiny.converters.uni.UniReactorConverters;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.queryhandling.QueryHandler;
import org.hibernate.reactive.mutiny.Mutiny;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.UUID;

@Slf4j
@Component
public class ProductQueryHandler {

    private final Mutiny.SessionFactory sessionFactory;

    private final IProductRepository productRepository;

    private final ProductMapper productMapper;

    private final ProductCacheService cacheService;


    public ProductQueryHandler(
            Mutiny.SessionFactory sessionFactory,
            IProductRepository productRepository,
            ProductMapper productMapper,
            ProductCacheService cacheService) {
        this.sessionFactory = sessionFactory;
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.cacheService = cacheService;
    }

    @QueryHandler
    public Mono<ProductDTO> handle(ProductByIdQuery productByIdQuery) {
        return cacheService.find(productByIdQuery.getId())
                .switchIfEmpty(Mono.defer(()-> sessionFactory.withSession(session ->
                                productRepository.findById(
                                        session,
                                        UUID.fromString(productByIdQuery.getId())))
                        .convert().with(UniReactorConverters.toMono())
                        .map(productMapper::mapToProductDTO)
                        .flatMap(productDTO -> cacheService.save(productByIdQuery.getId(),
                                productDTO,
                                Duration.ofDays(30))
                                .thenReturn(productDTO))));
    }

    @QueryHandler
    public Flux<ProductDTO> handle(ProductSearchQuery productSearchQuery) {
        return cacheService.findAll( productSearchQuery.getPageRequest(),
                                productSearchQuery.getSearchCriteria(),
                                productSearchQuery.getProductFilterCriteria())
                .switchIfEmpty(Flux.defer(()-> sessionFactory.withSession(session ->
                                productRepository.findAll(
                                        session,
                                        productSearchQuery.getPageRequest(),
                                        productSearchQuery.getSearchCriteria(),
                                        productSearchQuery.getProductFilterCriteria()))
                        .convert().with(UniReactorConverters.toFlux())
                        .flatMap(Flux::fromIterable)
                        .map(productMapper::mapToProductDTO)
                        .flatMap(productDTO->  cacheService.save(productDTO.getId().toString(),
                                productDTO,
                                Duration.ofDays(30)).thenReturn(productDTO))
                        .switchIfEmpty(Mono.defer(() ->
                                Mono.error(new ProductNotFoundException("Product not found"))))));
    }

    @QueryHandler
    public Mono<Long> handle(ProductQueryCountAll productQueryCountAll) {
        return sessionFactory.withSession(session ->
                        productRepository.count(
                                session,
                                productQueryCountAll.getSearchCriteria(),
                                productQueryCountAll.getProductFilterCriteria()))
                .convert().with(UniReactorConverters.toMono());
    }
}
