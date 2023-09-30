package com.lfhardware.product.service;

import com.lfhardware.product.ProductForm;
import com.lfhardware.product.domain.Product;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IProductService {

    Flux<Product> findAll(Pageable pageable);

    Mono<Product> findById(Long id);

    Mono<Product> save(Product product);

    Mono<Product> updateById(Long id, ProductForm productForm);
}
