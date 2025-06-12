package com.lfhardware.product.service;

import com.lfhardware.core.dto.PageRequest;
import com.lfhardware.core.repository.Search;
import com.lfhardware.product.dto.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

public interface IProductService {

    Flux<ProductDTO> findAll(PageRequest pageRequest, Search searchCriteria, ProductFilterCriteria filterCriteria);

    Mono<Long> count(Search searchCriteria, ProductFilterCriteria filterCriteria);

//    Mono<Pageable<ProductDTO>> findAllWithAvailableStock(ProductPageRequest productPageRequest);



    //    @Override
    //    public Mono<Pageable<ProductDTO>> findAllWithAvailableStock(ProductPageRequest pageRequest) {
    //
    //        Mono<List<Product>> pageableMono = Mono.fromFuture(sessionFactory.withSession(session -> productRepository.findAllRightJoinStock(session, pageRequest)).toCompletableFuture());
    //
    //        Mono<Long> countMono = Mono.fromFuture(sessionFactory.withSession(session -> productRepository.countRightJoinStock(session, pageRequest)).toCompletableFuture());
    //
    //        return Mono.justOrEmpty(cacheManager.getCache("productCache").get(pageRequest, (Callable<Pageable<ProductDTO>>) Pageable::new))
    //                .flatMap(product -> product.getCurrentPage() == 0 && product.getSize() == 0 ? Mono.empty() : Mono.just(product))
    //                .switchIfEmpty(Mono.defer(() -> pageableMono.zipWith(countMono).flatMap(zip -> {
    //                    Pageable<ProductDTO> productPageable = new Pageable<>(zip.getT1().stream().map(productMapper::mapToProductDTO).collect(Collectors.toList()), pageRequest.getPageSize(), pageRequest.getPage(), zip.getT2().intValue());
    //                    cacheManager.getCache("productCache").put(pageRequest, productPageable);
    //                    return Mono.just(productPageable);
    //                }))).log();
    //    }


    //    @Override
    //    public Mono<Pageable<ProductDTO>> findAllWithAvailableStock(ProductPageRequest pageRequest) {
    //
    //        Mono<List<Product>> pageableMono = Mono.fromFuture(sessionFactory.withSession(session -> productRepository.findAllRightJoinStock(session, pageRequest)).toCompletableFuture());
    //
    //        Mono<Long> countMono = Mono.fromFuture(sessionFactory.withSession(session -> productRepository.countRightJoinStock(session, pageRequest)).toCompletableFuture());
    //
    //        return Mono.justOrEmpty(cacheManager.getCache("productCache").get(pageRequest, (Callable<Pageable<ProductDTO>>) Pageable::new))
    //                .flatMap(product -> product.getCurrentPage() == 0 && product.getSize() == 0 ? Mono.empty() : Mono.just(product))
    //                .switchIfEmpty(Mono.defer(() -> pageableMono.zipWith(countMono).flatMap(zip -> {
    //                    Pageable<ProductDTO> productPageable = new Pageable<>(zip.getT1().stream().map(productMapper::mapToProductDTO).collect(Collectors.toList()), pageRequest.getPageSize(), pageRequest.getPage(), zip.getT2().intValue());
    //                    cacheManager.getCache("productCache").put(pageRequest, productPageable);
    //                    return Mono.just(productPageable);
    //                }))).log();
    //    }
    Mono<ProductDTO> findById(UUID id);

    //Mono<Void> save(Product product);

    Mono<ProductDTO> save(ProductInput productInput);

    Mono<Void> updateById(UUID id, ProductInput productInput);


    Mono<List<CategoryDTO>> findAllProductCategory();

    Mono<List<BrandDTO>> findAllProductBrand();

    Mono<ProductDTO> findByName(String name);

    Mono<Void> deleteById(UUID id);

    Mono<Void> rollback(UUID id);
}
