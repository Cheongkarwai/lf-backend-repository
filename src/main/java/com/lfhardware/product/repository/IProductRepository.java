package com.lfhardware.product.repository;

import com.lfhardware.product.domain.Product;
import com.lfhardware.product.dto.ProductPageRequest;
import com.lfhardware.shared.CrudRepository;
import com.lfhardware.shared.PageInfo;
import org.hibernate.reactive.stage.Stage;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public interface IProductRepository extends CrudRepository<Product,Long> {


    CompletionStage<List<Product>> findAll(Stage.Session session, ProductPageRequest productPageRequest);

    CompletionStage<Long> count(Stage.Session session, ProductPageRequest productPageRequest);

    CompletionStage<Product> findByName(Stage.Session session, String name);
}
