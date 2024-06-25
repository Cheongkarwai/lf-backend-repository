package com.lfhardware.product.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lfhardware.product.domain.Brand;
import com.lfhardware.product.domain.Category;
import com.lfhardware.product.domain.ProductImage;
import com.lfhardware.product.dto.*;
import com.lfhardware.product.mapper.BrandMapper;
import com.lfhardware.product.mapper.CategoryMapper;
import com.lfhardware.product.mapper.ProductMapper;
import com.lfhardware.product.mapper.StockMapper;
import com.lfhardware.product.repository.IBrandRepository;
import com.lfhardware.product.repository.ICategoryRepository;
import com.lfhardware.product.repository.IProductRepository;
import com.lfhardware.shared.Pageable;
import com.lfhardware.stock.domain.Stock;
import com.lfhardware.stock.repository.IStockRepository;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.reactive.stage.Stage;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import software.amazon.awssdk.services.s3.S3AsyncClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductService implements IProductService {

    private final IProductRepository productRepository;

    private final ICategoryRepository categoryRepository;

    private final IBrandRepository brandRepository;

    private final IStockRepository stockRepository;

    private final ProductMapper productMapper;

    private final StockMapper stockMapper;

    private final CategoryMapper categoryMapper;

    private final BrandMapper brandMapper;

    private final CacheManager cacheManager;

    private final Stage.SessionFactory sessionFactory;

    private final ObjectMapper objectMapper;

    private final S3AsyncClient s3AsyncClient;

    private final String PRODUCT_CACHE = "productCache";


    public ProductService(ICategoryRepository categoryRepository, IProductRepository productRepository, IBrandRepository brandRepository, IStockRepository stockRepository,
                          ProductMapper productMapper, StockMapper stockMapper, CategoryMapper categoryMapper, BrandMapper brandMapper,
                          CacheManager cacheManager, Stage.SessionFactory sessionFactory, S3AsyncClient s3AsyncClient, ObjectMapper objectMapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.stockRepository = stockRepository;
        this.brandRepository = brandRepository;
        this.categoryMapper = categoryMapper;
        this.productMapper = productMapper;
        this.stockMapper = stockMapper;
        this.brandMapper = brandMapper;
        this.cacheManager = cacheManager;
        this.sessionFactory = sessionFactory;
        this.s3AsyncClient = s3AsyncClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<Pageable<ProductDTO>> findAll(ProductPageRequest pageRequest) {

        Mono<List<ProductDTO>> pageableMono = Mono.fromFuture(sessionFactory.withSession(session -> productRepository.findAll(session, pageRequest)
                .thenApply(products-> products.stream().map(productMapper::mapToProductDTO).collect(Collectors.toList()))).toCompletableFuture());

        Mono<Long> countMono = Mono.fromFuture(sessionFactory.withSession(session -> productRepository.count(session, pageRequest)).toCompletableFuture());

        return Mono.justOrEmpty(cacheManager.getCache(PRODUCT_CACHE).get(pageRequest, (Callable<Pageable<ProductDTO>>) Pageable::new))
                .flatMap(product -> product.getCurrentPage() == 0 && product.getSize() == 0 ? Mono.empty() : Mono.just(product))
                .switchIfEmpty(Mono.defer(() -> pageableMono.zipWith(countMono).flatMap(zip -> {
                    Pageable<ProductDTO> productPageable = new Pageable<>(zip.getT1(), pageRequest.getPageSize(), pageRequest.getPage(), zip.getT2().intValue());
                    cacheManager.getCache(PRODUCT_CACHE).put(pageRequest, productPageable);
                    return Mono.just(productPageable);
                }))).log();
    }

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
    @Override
    public Mono<ProductDTO> findById(Long id) {
        return Mono.fromCompletionStage(sessionFactory.withSession(session -> productRepository.findById(session, id)
                .thenApply(productMapper::mapToProductDTO)));
    }

    @Override
    public Mono<ProductDTO> findByName(String name) {
        return Mono.fromCompletionStage(sessionFactory.withSession(session -> productRepository.findByName(session, name)
                .thenApply(productMapper::mapToProductDTO)));
    }

    @Override
    public Mono<Void> save(ProductInput productInput) {

        Set<Stock> stocks = productInput.getStocks().stream().map(stockMapper::mapToStock).collect(Collectors.toSet());
        Set<ProductImage> productImages = productInput.getImages().stream().map(productMapper::mapToProductImage).collect(Collectors.toSet());

        return Mono.fromCallable(() -> {
                    this.cacheManager.getCache(PRODUCT_CACHE).clear();
                    return productMapper.mapToProduct(productInput);
                }).subscribeOn(Schedulers.boundedElastic())
                .flatMap(product -> {;
                    return Mono.fromCompletionStage(sessionFactory
                            .withTransaction((session, transaction) -> {
                                product.setStocks(stocks);
                                product.setProductImages(productImages);
                                return productRepository.save(session, product);
                            }));
                });
    }


    @Override
    public Mono<Void> updateById(Long id, ProductInput productInput) {

        Set<Stock> stocks = productInput.getStocks().stream().map(stockMapper::mapToStock).collect(Collectors.toSet());
        Set<ProductImage> productImages = productInput.getImages().stream().map(productMapper::mapToProductImage).collect(Collectors.toSet());
        Category category = categoryMapper.mapToCategory(productInput.getCategory());
        Brand brand = brandMapper.mapToBrand(productInput.getBrand());

        return Mono.fromCompletionStage(sessionFactory.withTransaction(session -> productRepository.findById(session, id)
                .thenCompose(product -> {
                    product.setName(productInput.getName());
                    product.setDescription(productInput.getDescription());
                    product.setPrice(productInput.getPrice());
                    product.setCategory(category);
                    product.setBrand(brand);
                    product.setStocks(stocks);
                    product.setProductImages(productImages);
                    return productRepository.save(session, product);
        })));
    }

    @Override
    public Mono<List<CategoryDTO>> findAllProductCategory() {
        log.info("Finding products category");
        return Mono.justOrEmpty(cacheManager.getCache("categoryCache").get("categories", (Callable<List<CategoryDTO>>) ArrayList::new))
                .flatMap(categoryDTOS -> categoryDTOS.size() > 0 ? Mono.just(categoryDTOS) : Mono.empty())
                .switchIfEmpty(Mono.defer(() -> Mono.fromCompletionStage(sessionFactory.withSession(session -> categoryRepository.findAll(session)
                        .thenApply(categories -> {
                            List<CategoryDTO> categoryDTOS = categories.stream().map(categoryMapper::mapToCategoryDTO).collect(Collectors.toList());
                            cacheManager.getCache("categoryCache").put("categories", categoryDTOS);
                            return categoryDTOS;
                        })))));
    }

    @Override
    public Mono<List<BrandDTO>> findAllProductBrand() {
        log.info("Finding products brand");
        return Mono.justOrEmpty(cacheManager.getCache("brandCache").get("brands", (Callable<List<BrandDTO>>) ArrayList::new))
                .flatMap(categoryDTOS -> categoryDTOS.size() > 0 ? Mono.just(categoryDTOS) : Mono.empty())
                .switchIfEmpty(Mono.defer(() -> Mono.fromCompletionStage(sessionFactory.withSession(session -> brandRepository.findAll(session)
                        .thenApply(brands -> {
                            List<BrandDTO> brandDTOS = brands.stream().map(brandMapper::mapToBrandDTO).collect(Collectors.toList());
                            cacheManager.getCache("brandCache").put("brands", brandDTOS);
                            return brandDTOS;
                        })))));
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        return Mono.fromCompletionStage(
                sessionFactory.withTransaction((session, transaction) -> {
                    log.info("Deleting product {}", id);
                    return productRepository.deleteById(session, id);
                }).thenAccept((e) -> {
                    this.cacheManager.getCache(PRODUCT_CACHE).clear();
                }));
    }
}
