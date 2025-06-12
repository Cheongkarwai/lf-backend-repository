package com.lfhardware.product.query.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lfhardware.core.dto.PageRequest;
import com.lfhardware.core.repository.Search;
import com.lfhardware.product.dto.ProductDTO;
import com.lfhardware.product.dto.ProductFilterCriteria;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.*;
import org.redisson.api.search.index.FieldIndex;
import org.redisson.api.search.index.IndexOptions;
import org.redisson.api.search.index.IndexType;
import org.redisson.api.search.query.QueryOptions;
import org.redisson.api.search.query.SearchResult;
import org.redisson.client.codec.Codec;
import org.redisson.client.codec.StringCodec;
import org.redisson.codec.JacksonCodec;
import org.redisson.codec.JsonJacksonCodec;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductCacheService {

    private final RedissonReactiveClient redissonReactiveClient;

    private final Codec codec = new JsonJacksonCodec();

    private final ObjectMapper objectMapper;

    public ProductCacheService(RedissonReactiveClient redissonReactiveClient, ObjectMapper objectMapper) {
        this.redissonReactiveClient = redissonReactiveClient;
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void init(){
        RSearchReactive searchReactive = this.redissonReactiveClient.getSearch(StringCodec.INSTANCE);
        searchReactive.createIndex("idx:product", IndexOptions.defaults()
                        .on(IndexType.JSON)
                        .prefix(List.of("product:")),
                FieldIndex.tag("$.id").as("id"),
                FieldIndex.numeric("$.brand.id").as("brand"),
                FieldIndex.numeric("$.category.id").as("category"))
                .subscribe();
    }

    public Mono<Void> save(String id, ProductDTO productDTO, Duration ttl) {
        RJsonBucketReactive<ProductDTO> jsonBucketReactive = this.redissonReactiveClient
                .getJsonBucket(parseKey(id, CacheName.PRODUCT),
                        new JacksonCodec<>(objectMapper, ProductDTO.class));
        return jsonBucketReactive.set(productDTO, ttl);
    }

    public Flux<Void> saveAll(Flux<ProductDTO> productDTOFlux, Duration ttl) {
        return productDTOFlux
                .flatMap(productDTO -> {
                    RJsonBucketReactive<ProductDTO> bucket = redissonReactiveClient.getJsonBucket(
                            parseKey(productDTO.getId().toString(), CacheName.PRODUCT),
                            new JacksonCodec<>(objectMapper, ProductDTO.class));
                    return bucket.set(productDTO, ttl);
                });
    }

    public Mono<ProductDTO> find(String id) {
        RJsonBucketReactive<ProductDTO> jsonBucketReactive = this.redissonReactiveClient.getJsonBucket(
                parseKey(id, CacheName.PRODUCT),
                new JacksonCodec<>(objectMapper, ProductDTO.class));

        return jsonBucketReactive.get();
    }

    public Flux<ProductDTO> findAll(PageRequest pageRequest, Search searchCriteria, ProductFilterCriteria productFilterCriteria) {

        log.info("Find all");

        RSearchReactive searchReactive = this.redissonReactiveClient.getSearch(StringCodec.INSTANCE);

        QueryOptions queryOptions = QueryOptions.defaults();

        queryOptions.limit(pageRequest.getPageNo(), pageRequest.getPageSize());

        if (Objects.nonNull(pageRequest.getSort())) {
            queryOptions.sortBy(pageRequest.getSort().getName())
                    .sortOrder(SortOrder.valueOf(pageRequest.getSort().getOrder().name()));
        }

        Mono<String> productFilterQuery = Mono.justOrEmpty(productFilterCriteria)
                .flatMap(filterCriteria -> {
                    if (Objects.nonNull(filterCriteria.getBrandIds()) && !filterCriteria.getBrandIds().isEmpty()) {
                        return Mono.just(filterCriteria.getBrandIds())
                                .map(brandIds -> brandIds.stream()
                                        .map(brand -> String.format("%s", brand))
                                        .collect(Collectors.joining("|")))
                                .map(joinedBrands -> "@brand:{" + joinedBrands + "}");
                    }
                    return Mono.just("*");
                })
                .defaultIfEmpty("*");


        return productFilterQuery
                .flatMap(filterQuery -> {
                    log.info(filterQuery);
                    return searchReactive.search("idx:product", filterQuery, queryOptions)
                            .map(SearchResult::getDocuments);
                })
                .flatMapMany(documents -> Flux.fromIterable(documents)
                        .map(document -> objectMapper.convertValue(document.getAttributes(), ProductDTO.class)));
    }

    private static String parseKey(String id, CacheName cacheName) {
        return String.format("%s:%s", cacheName.getName(), id);
    }

}
