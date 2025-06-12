package com.lfhardware.product.repository;

import com.lfhardware.core.dto.PageRequest;
import com.lfhardware.core.repository.Search;
import com.lfhardware.product.domain.Product;
import com.lfhardware.product.dto.ProductFilterCriteria;
import io.smallrye.mutiny.Uni;
import org.hibernate.reactive.mutiny.Mutiny;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.List;
import java.util.UUID;

public interface IProductRepository extends MutinyCrudRepository<Product, UUID> {


    Uni<List<Product>> findAll(Mutiny.Session session, PageRequest pageRequest, Search searchRequest, ProductFilterCriteria filterCriteria);

    Uni<Long> count(Mutiny.Session session, Search search, ProductFilterCriteria filterCriteria);

    Uni<Product> findByName(Mutiny.Session session, String name);

}
