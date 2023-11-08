package com.lfhardware.product.service;

import com.lfhardware.product.ProductForm;
import com.lfhardware.product.domain.Product;
import com.lfhardware.shared.PageInfo;
import com.lfhardware.shared.Pageable;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IProductService {

    Mono<Pageable<Product>> findAll(PageInfo pageInfo);

    Mono<Product> findById(Long id);

    Mono<Product> save(Product product);

    Mono<Product> updateById(Long id, ProductForm productForm);
}
