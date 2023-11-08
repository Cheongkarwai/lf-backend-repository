package com.lfhardware.product.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lfhardware.product.ProductForm;
import com.lfhardware.product.domain.Product;
import com.lfhardware.product.repository.IProductRepository;
import com.lfhardware.shared.PageInfo;
import com.lfhardware.shared.Pageable;
import jakarta.persistence.Persistence;
import org.hibernate.reactive.stage.Stage;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class ProductService implements  IProductService{

    private ObjectMapper objectMapper;
    private final IProductRepository productRepository;

    private Stage.SessionFactory sessionFactory = Persistence.createEntityManagerFactory("postgres").unwrap(Stage.SessionFactory.class);

    public ProductService(ObjectMapper objectMapper,IProductRepository productRepository){
        this.objectMapper = objectMapper;
        this.productRepository = productRepository;
    }

    @Override
    public Mono<Pageable<Product>> findAll(PageInfo pageInfo) {

       Mono<List<Product>> pageableMono = Mono.fromFuture(sessionFactory.withSession(session->productRepository.findAll(session,pageInfo))
               .toCompletableFuture());

       Mono<Long> countMono = Mono.fromFuture(sessionFactory.withSession(session -> productRepository.count(session,pageInfo))
               .toCompletableFuture());

       return Mono.zip(pageableMono,countMono)
               .flatMap(zip->Mono.just(new Pageable<>(zip.getT1(), pageInfo.getPageSize(),
                       pageInfo.getPage(),zip.getT2().intValue())));
    }

    @Override
    public Mono<Product> findById(Long id){
        return Mono.fromFuture(productRepository.findById(id));
    }

    @Override
    public Mono<Product> save(Product product) {
        return Mono.empty();
       // return productRepository.save(product);
    }

    @Override
    public Mono<Product> updateById(Long id, ProductForm productForm) {
        return Mono.empty();
//        return productRepository.findById(id).switchIfEmpty(Mono.error(new NoSuchElementException("Product not found")))
//                .flatMap(e-> {
//                    try {
//                        BeanUtils.copyProperties(e,productForm);
//                       return productRepository.save(e);
//                    } catch (IllegalAccessException | InvocationTargetException ex) {
//                        return Mono.error(new RuntimeException(ex));
//                    }
//                });
    }

}
