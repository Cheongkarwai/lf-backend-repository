package com.lfhardware.product.service;

import com.lfhardware.file.dto.ImageDTO;
import com.lfhardware.product.dto.*;
import com.lfhardware.shared.Pageable;
import org.springframework.http.codec.multipart.Part;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IProductService {

    Mono<Pageable<ProductDTO>> findAll(ProductPageRequest productPageRequest);

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
    Mono<ProductDTO> findById(Long id);

    //Mono<Void> save(Product product);

    Mono<Void> save(ProductInput productInput);

    Mono<Void> updateById(Long id, ProductInput productInput);


    Mono<List<CategoryDTO>> findAllProductCategory();

    Mono<List<BrandDTO>> findAllProductBrand();

    Mono<ProductDTO> findByName(String name);

    Mono<Void> deleteById(Long id);
}
