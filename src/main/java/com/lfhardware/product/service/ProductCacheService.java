//package com.lfhardware.product.service;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.lfhardware.product.dto.ProductDTO;
//import com.lfhardware.product.mapper.ProductMapper;
//import org.springframework.data.redis.core.ReactiveRedisOperations;
//import org.springframework.stereotype.Service;
//import reactor.core.publisher.Mono;
//
//import java.util.Map;
//
//@Service
//public class ProductCacheService {
//
//    private final ReactiveRedisOperations<String, ProductDTO> productRedisOps;
//    private final ProductMapper productMapper;
//    private final ObjectMapper jacksonObjectMapper;
//
//    public ProductCacheService(ReactiveRedisOperations<String, ProductDTO> productRedisOps,
//                               ProductMapper productMapper, ObjectMapper jacksonObjectMapper) {
//        this.productRedisOps = productRedisOps;
//        this.productMapper = productMapper;
//        this.jacksonObjectMapper = jacksonObjectMapper;
//    }
//
//    public Mono<ProductDTO> findById(String id) {
//        String key = parseKey(id);
//        return productRedisOps.opsForHash()
//                .entries(key)
//                .collectMap(Map.Entry::getKey, Map.Entry::getValue)
//                .map(productMapper::mapToProductDTO);
//    }
//
//
//    private String parseKey(String id) {
//        String CACHE_NAME = "product";
//        return String.format("%s:%s", CACHE_NAME, id);
//    }
//
//    public Mono<Boolean> save(ProductDTO productDTO) {
//        return productRedisOps.opsForHash()
//                .putAll(parseKey(String.valueOf(productDTO.getId())),
//                        jacksonObjectMapper.convertValue(productDTO, Map.class));
//    }
//}
