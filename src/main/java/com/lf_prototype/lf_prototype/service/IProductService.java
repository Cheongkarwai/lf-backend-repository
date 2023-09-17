package com.lf_prototype.lf_prototype.service;

import com.lf_prototype.lf_prototype.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IProductService {

    Flux<Product> findAll(Pageable pageable);
}
