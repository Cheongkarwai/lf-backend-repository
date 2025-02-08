package com.lfhardware.product.repository;

import com.lfhardware.product.domain.Product;
import com.lfhardware.product.dto.ProductPageRequest;
import com.lfhardware.core.repository.CrudRepository;
import io.smallrye.mutiny.Uni;
import org.hibernate.reactive.mutiny.Mutiny;
import org.hibernate.reactive.stage.Stage;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletionStage;

public interface IProductRepository extends MutinyCrudRepository<Product, UUID> {


    Uni<List<Product>> findAll(Mutiny.Session session, ProductPageRequest productPageRequest);

    Uni<Long> count(Mutiny.Session session, ProductPageRequest productPageRequest);

    Uni<Product> findByName(Mutiny.Session session, String name);
}
