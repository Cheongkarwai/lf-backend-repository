package com.lfhardware.product.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lfhardware.product.ProductForm;
import com.lfhardware.product.domain.Product;
import com.lfhardware.product.repository.ProductRepository;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.reflect.InvocationTargetException;
import java.util.NoSuchElementException;

@Service
@Transactional
public class ProductService implements  IProductService{

    private ProductRepository productRepository;

    private ObjectMapper objectMapper;

    public ProductService(ProductRepository productRepository,ObjectMapper objectMapper){
        this.productRepository = productRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public Flux<Product> findAll(Pageable pageable) {
        return productRepository.findBy(pageable);
    }

    @Override
    public Mono<Product> findById(Long id){
        return productRepository.findById(id);
    }

    @Override
    public Mono<Product> save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Mono<Product> updateById(Long id, ProductForm productForm) {
        return productRepository.findById(id).switchIfEmpty(Mono.error(new NoSuchElementException("Product not found")))
                .flatMap(e-> {
                    try {
                        BeanUtils.copyProperties(e,productForm);
                       return productRepository.save(e);
                    } catch (IllegalAccessException | InvocationTargetException ex) {
                        return Mono.error(new RuntimeException(ex));
                    }
                });
    }

}
