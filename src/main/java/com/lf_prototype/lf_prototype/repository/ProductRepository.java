package com.lf_prototype.lf_prototype.repository;

import com.lf_prototype.lf_prototype.domain.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import reactor.core.publisher.Flux;

public interface ProductRepository extends ReactiveSortingRepository<Product,Long> , ReactiveCrudRepository<Product,Long> {

    Flux<Product> findBy(Pageable pageable);
}
