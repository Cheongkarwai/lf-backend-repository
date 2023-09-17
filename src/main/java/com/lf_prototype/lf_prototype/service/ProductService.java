package com.lf_prototype.lf_prototype.service;

import com.lf_prototype.lf_prototype.domain.Product;
import com.lf_prototype.lf_prototype.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class ProductService implements  IProductService{

    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    @Override
    public Flux<Product> findAll(Pageable pageable) {
        return productRepository.findBy(pageable);
    }

}
