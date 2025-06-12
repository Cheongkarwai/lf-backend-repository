package com.lfhardware.product.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lfhardware.authorization.resource.service.IResourceService;
import com.lfhardware.configuration.CacheConfiguration;
import com.lfhardware.core.dto.PageRequest;
import com.lfhardware.core.repository.Search;
import com.lfhardware.product.domain.Brand;
import com.lfhardware.product.domain.Category;
import com.lfhardware.product.domain.ProductImage;
import com.lfhardware.product.dto.*;
import com.lfhardware.product.event.ProductEvent;
import com.lfhardware.product.event.ProductEventPublisher;
import com.lfhardware.product.exception.ProductNotFoundException;
import com.lfhardware.product.mapper.BrandMapper;
import com.lfhardware.product.mapper.CategoryMapper;
import com.lfhardware.product.mapper.ProductMapper;
import com.lfhardware.product.mapper.StockMapper;
import com.lfhardware.product.query.service.ProductCacheService;
import com.lfhardware.product.repository.IBrandRepository;
import com.lfhardware.product.repository.ICategoryRepository;
import com.lfhardware.product.repository.IProductRepository;
import com.lfhardware.stock.domain.Stock;
import com.lfhardware.stock.repository.IStockRepository;
import io.smallrye.mutiny.converters.uni.UniReactorConverters;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.reactive.mutiny.Mutiny;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Caching;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.s3.S3AsyncClient;

import java.util.List;
import java.util.Set;
import java.util.UUID;
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

    private final Mutiny.SessionFactory sessionFactory;

    private final ObjectMapper objectMapper;

    private final S3AsyncClient s3AsyncClient;

    private final String PRODUCT_CACHE = "products";

    private final IResourceService resourceService;

    private final StreamBridge streamBridge;

    private final ProductEventPublisher productEventPublisher;

    private final ProductCacheService productCacheService;


    public ProductService(ICategoryRepository categoryRepository, IProductRepository productRepository, IBrandRepository brandRepository, IStockRepository stockRepository,
                          ProductMapper productMapper, StockMapper stockMapper, CategoryMapper categoryMapper, BrandMapper brandMapper,
                          CacheManager cacheManager, Mutiny.SessionFactory sessionFactory, S3AsyncClient s3AsyncClient, ObjectMapper objectMapper,
                          IResourceService resourceService, StreamBridge streamBridge, ProductEventPublisher productEventPublisher, ProductCacheService productCacheService) {
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
        this.resourceService = resourceService;
        this.streamBridge = streamBridge;
        this.productEventPublisher = productEventPublisher;
        this.productCacheService = productCacheService;
    }

    @Override
    public Mono<Long> count(Search searchCriteria, ProductFilterCriteria filterCriteria) {
        return sessionFactory.withSession(session -> productRepository.count(session, searchCriteria, filterCriteria))
                .convert().with(UniReactorConverters.toMono());
    }

    @Override
    public Flux<ProductDTO> findAll(PageRequest pageRequest, Search searchCriteria, ProductFilterCriteria filterCriteria) {
        return sessionFactory.withSession(session -> productRepository.findAll(session, pageRequest, searchCriteria, filterCriteria))
                .convert().with(UniReactorConverters.toFlux())
                .flatMap(Flux::fromIterable)
                .map(productMapper::mapToProductDTO)
                .switchIfEmpty(Mono.defer(() -> Mono.error(new ProductNotFoundException("Product not found"))));
    }

    @Override
    public Mono<ProductDTO> findById(UUID id) {
//        return productCacheService.findById(id.toString())
//                .switchIfEmpty(sessionFactory.withSession(session -> productRepository.findById(session, id)
//                                .onItem().ifNull().failWith(new ProductNotFoundException("Product not found")))
//                        .convert().with(UniReactorConverters.toMono())
//                        .map(productMapper::mapToProductDTO)
//                        .flatMap(product -> productCacheService.save(product).thenReturn(product)));
        return Mono.empty();
    }

    @Override
    public Mono<ProductDTO> findByName(String name) {
        return sessionFactory.withSession(session -> productRepository.findByName(session, name)
                        .map(productMapper::mapToProductDTO))
                .convert().with(UniReactorConverters.toMono());
    }

    @Override
    public Mono<ProductDTO> save(ProductInput productInput) {

        return ReactiveSecurityContextHolder.getContext()
                .switchIfEmpty(Mono.defer(() -> Mono.error(new AuthenticationCredentialsNotFoundException("Security context is empty"))))
                .flatMap(securityContext -> {
                   /* if (securityContext.getAuthentication() != null && securityContext.getAuthentication().getCredentials() instanceof Jwt jwt) {

                        Product product = productMapper.mapToProduct(productInput);

                        return sessionFactory.withTransaction(session -> productRepository.save(session, product)
                                        .onFailure().transform(failure -> new RuntimeException(failure.getMessage()))
                                )
                                .convert().with(UniReactorConverters.toMono())
                                .thenReturn(product)
                                .flatMap(savedProduct -> {
                                    log.info("Saving into redis cache {}", objectMapper.convertValue(product, Map.class));
                                    return productReactiveRedisOps.opsForHash().putAll("product:" + product.getId(), objectMapper.convertValue(product, Map.class))
                                            .thenReturn(savedProduct);
                                })
                                .map(e->productMapper.mapToProductDTO(e))
                                .flatMap(productDTO -> productEventPublisher.publishProductCreatedEvent(ProductEvent.builder()
                                                .jwt(jwt).productDTO(productDTO).eventType(ProductEvent.EventType.CREATED).build())
                                        .thenReturn(productDTO));

                    }*/
                    return Mono.error(new AuthenticationCredentialsNotFoundException("Authentication is empty"));
                });
    }


    @Override
    @Caching(
            put = @CachePut(value = CacheConfiguration.productCache, key = "#id"),
            evict = {
                    @CacheEvict(value = CacheConfiguration.productsCache)
            }
    )
    public Mono<Void> updateById(UUID id, ProductInput productInput) {

        log.info("Updating product id={},{}", id, productInput.toString());

        Set<Stock> stocks = productInput.getStocks().stream().map(stockMapper::mapToStock).collect(Collectors.toSet());
        Set<ProductImage> productImages = productInput.getImages().stream().map(productMapper::mapToProductImage).collect(Collectors.toSet());
        Category category = categoryMapper.mapToCategory(productInput.getCategory());
        Brand brand = brandMapper.mapToBrand(productInput.getBrand());

        return sessionFactory.withTransaction(session -> productRepository.findById(session, id)
                        .map(product -> {
                            productMapper.mapToProduct(productInput);
                            if (productInput.getName() != null) {
                                product.setName(productInput.getName());
                            }
                            if (productInput.getPrice() != null) {
                                product.setPrice(productInput.getPrice());
                            }
                            if (productInput.getDescription() != null) {
                                product.setDescription(productInput.getDescription());
                            }
                            return product;
                        })
                        .onItem().ifNull().failWith(new ProductNotFoundException("Product not found"))
                        .chain(product -> productRepository.save(session, product)))
                .convert().with(UniReactorConverters.toMono());
    }

    @Override
    public Mono<List<CategoryDTO>> findAllProductCategory() {
        log.info("Finding products category");
//        return Mono.justOrEmpty(cacheManager.getCache("categoryCache").get("categories", (Callable<List<CategoryDTO>>) ArrayList::new))
//                .flatMap(categoryDTOS -> categoryDTOS.size() > 0 ? Mono.just(categoryDTOS) : Mono.empty())
//                .switchIfEmpty(Mono.defer(() -> Mono.fromCompletionStage(sessionFactory.withSession(session -> categoryRepository.findAll(session)
//                        .thenApply(categories -> {
//                            List<CategoryDTO> categoryDTOS = categories.stream().map(categoryMapper::mapToCategoryDTO).collect(Collectors.toList());
//                            cacheManager.getCache("categoryCache").put("categories", categoryDTOS);
//                            return categoryDTOS;
//                        })))));
        return Mono.empty();
    }

    @Override
    public Mono<List<BrandDTO>> findAllProductBrand() {
        log.info("Finding products brand");
//        return Mono.justOrEmpty(cacheManager.getCache("brandCache").get("brands", (Callable<List<BrandDTO>>) ArrayList::new))
//                .flatMap(categoryDTOS -> categoryDTOS.size() > 0 ? Mono.just(categoryDTOS) : Mono.empty())
//                .switchIfEmpty(Mono.defer(() -> Mono.fromCompletionStage(sessionFactory.withSession(session -> brandRepository.findAll(session)
//                        .thenApply(brands -> {
//                            List<BrandDTO> brandDTOS = brands.stream().map(brandMapper::mapToBrandDTO).collect(Collectors.toList());
//                            cacheManager.getCache("brandCache").put("brands", brandDTOS);
//                            return brandDTOS;
//                        })))));
        return Mono.empty();
    }

    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = CacheConfiguration.productsCache),
                    @CacheEvict(value = CacheConfiguration.productCache, key = "#id")
            }
    )
    public Mono<Void> deleteById(UUID id) {

        return ReactiveSecurityContextHolder.getContext()
                .switchIfEmpty(Mono.defer(() -> Mono.error(new AuthenticationCredentialsNotFoundException("Security context is empty"))))
                .flatMap(securityContext -> {
                    if (securityContext.getAuthentication() != null && securityContext.getAuthentication().getCredentials() instanceof Jwt jwt) {
                        return sessionFactory.withTransaction(session -> {
                                    return productRepository.findById(session, id)
                                            .onItem().ifNull().failWith(new ProductNotFoundException("Product not found"))
                                            .chain(product -> productRepository.delete(session, product).replaceWith(product))
                                            .invoke(product -> {
                                                log.info("Product deleted id={}", id);
                                                productEventPublisher.publishProductCreatedEvent(ProductEvent.builder()
                                                        .jwt(jwt).productDTO(productMapper.mapToProductDTO(product)).eventType(ProductEvent.EventType.DELETED).build());

                                            }).log();
                                })
                                .convert().with(UniReactorConverters.toMono())
                                .then();
                    }
                    return Mono.error(new AuthenticationCredentialsNotFoundException("Authentication is empty"));
                });
    }

    @Override
    public Mono<Void> rollback(UUID id) {
        return sessionFactory.withTransaction(session -> productRepository.findById(session, id)
                        .chain(product -> productRepository.delete(session, product)))
                .convert().with(UniReactorConverters.toMono());
    }
}
