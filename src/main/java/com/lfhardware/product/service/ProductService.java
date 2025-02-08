package com.lfhardware.product.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lfhardware.configuration.CacheConfiguration;
import com.lfhardware.authorization.resource.service.IResourceService;
import com.lfhardware.product.domain.Brand;
import com.lfhardware.product.domain.Category;
import com.lfhardware.product.domain.Product;
import com.lfhardware.product.domain.ProductImage;
import com.lfhardware.product.dto.*;
import com.lfhardware.product.event.ProductEvent;
import com.lfhardware.product.event.ProductEventPublisher;
import com.lfhardware.product.mapper.BrandMapper;
import com.lfhardware.product.mapper.CategoryMapper;
import com.lfhardware.product.mapper.ProductMapper;
import com.lfhardware.product.mapper.StockMapper;
import com.lfhardware.product.repository.IBrandRepository;
import com.lfhardware.product.repository.ICategoryRepository;
import com.lfhardware.product.repository.IProductRepository;
import com.lfhardware.stock.domain.Stock;
import com.lfhardware.stock.repository.IStockRepository;
import io.smallrye.mutiny.converters.uni.UniReactorConverters;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.reactive.mutiny.Mutiny;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
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

    public ProductService(ICategoryRepository categoryRepository, IProductRepository productRepository, IBrandRepository brandRepository, IStockRepository stockRepository,
                          ProductMapper productMapper, StockMapper stockMapper, CategoryMapper categoryMapper, BrandMapper brandMapper,
                          CacheManager cacheManager, Mutiny.SessionFactory sessionFactory, S3AsyncClient s3AsyncClient, ObjectMapper objectMapper,
                          IResourceService resourceService, StreamBridge streamBridge, ProductEventPublisher productEventPublisher) {
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
    }

    @Override
    public Mono<Long> count(ProductPageRequest productPageRequest) {
        return sessionFactory.withSession(session -> productRepository.count(session, productPageRequest))
                .convert().with(UniReactorConverters.toMono());
    }

    @Override
    public Flux<ProductDTO> findAll(ProductPageRequest productPageRequest) {

//        Mono<List<ProductDTO>> pageableMono = Mono.fromFuture(sessionFactory.withSession(session -> productRepository.findAll(session, pageRequest)
//                .thenApply(products -> products.stream().map(productMapper::mapToProductDTO).collect(Collectors.toList()))).toCompletableFuture());
//
//
//        return Mono.justOrEmpty(cacheManager.getCache(PRODUCT_CACHE).get(pageRequest, (Callable<Pageable<ProductDTO>>) Pageable::new))
//                .flatMap(product -> product.getCurrentPage() == 0 && product.getSize() == 0 ? Mono.empty() : Mono.just(product))
//                .switchIfEmpty(Mono.defer(() -> pageableMono.zipWith(countMono).flatMap(zip -> {
//                    Pageable<ProductDTO> productPageable = new Pageable<>(zip.getT1(), pageRequest.getPageSize(), pageRequest.getPage(), zip.getT2().intValue());
//                    cacheManager.getCache(PRODUCT_CACHE).put(pageRequest, productPageable);
//                    return Mono.just(productPageable);
//                }))).log();
        return sessionFactory.withSession(session -> productRepository.findAll(session, productPageRequest))
                .convert().with(UniReactorConverters.toFlux())
                .flatMap(Flux::fromIterable)
                .map(productMapper::mapToProductDTO);
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
    @Cacheable(value = CacheConfiguration.productCache, key = "#id")
    @Override
    public Mono<ProductDTO> findById(UUID id) {
        return sessionFactory.withSession(session -> productRepository.findById(session, id)
                        .map(productMapper::mapToProductDTO))
                .convert().with(UniReactorConverters.toMono());
    }

    @Override
    public Mono<ProductDTO> findByName(String name) {
//        return Mono.fromCompletionStage(sessionFactory.withSession(session -> productRepository.findByName(session, name)
//                .thenApply(productMapper::mapToProductDTO)));
        return Mono.empty();
    }

    @Override
    public Mono<ProductDTO> save(ProductInput productInput) {

//        Set<Stock> stocks = productInput.getStocks().stream().map(stockMapper::mapToStock).collect(Collectors.toSet());
//        Set<ProductImage> productImages = productInput.getImages().stream().map(productMapper::mapToProductImage).collect(Collectors.toSet());
//
        Product product = productMapper.mapToProduct(productInput);

        return ReactiveSecurityContextHolder.getContext()
                .switchIfEmpty(Mono.defer(() -> Mono.error(new AuthenticationCredentialsNotFoundException("Security context is empty"))))
                .flatMap(securityContext -> {
                    if (securityContext.getAuthentication() != null && securityContext.getAuthentication().getCredentials() instanceof Jwt jwt) {
                        return sessionFactory.withTransaction(session -> productRepository.save(session, product)
                                                .onFailure().transform(failure -> new RuntimeException(failure.getMessage()))
                                                .map(v -> productMapper.mapToProductDTO(product))
                                                .invoke(e -> {
                                                    productEventPublisher.publishProductCreatedEvent(ProductEvent.builder()
                                                            .jwt(jwt).productDTO(e).build());
                                                })
//                                        .chain(v -> {
//                                            Mono<Void> resourceCreationMono = resourceService.create(createProductResource(jwt, product));
//                                            Flow.Publisher<Void> resourceCreationPublisher = FlowAdapters.toFlowPublisher(resourceCreationMono);
//                                            return Uni.createFrom().publisher(resourceCreationPublisher);
//                                            // .emitOn(ContextInternal.current().executor());
//                                        })
                                )
                                .convert().with(UniReactorConverters.toMono());
                    }
                    return Mono.error(new AuthenticationCredentialsNotFoundException("Authentication is empty"));
                });
    }

//    public ResourceRepresentation createProductResource(Jwt jwt, ProductDTO product) {
//        ResourceRepresentation resourceRepresentation = new ResourceRepresentation();
//        resourceRepresentation.setId(String.valueOf(product.getId()));
//        resourceRepresentation.setOwner(jwt.getSubject());
//        resourceRepresentation.setOwnerManagedAccess(true);
//        resourceRepresentation.setDisplayName(product.getName());
//        resourceRepresentation.setName("Product " + product.getId());
//        resourceRepresentation.setType(ResourceType.PRODUCT);
//        resourceRepresentation.setUri("/api/v1/products/" + product.getId());
//        return resourceRepresentation;
//    }


    @Override
    public Mono<Void> updateById(UUID id, ProductInput productInput) {

        Set<Stock> stocks = productInput.getStocks().stream().map(stockMapper::mapToStock).collect(Collectors.toSet());
        Set<ProductImage> productImages = productInput.getImages().stream().map(productMapper::mapToProductImage).collect(Collectors.toSet());
        Category category = categoryMapper.mapToCategory(productInput.getCategory());
        Brand brand = brandMapper.mapToBrand(productInput.getBrand());

        return sessionFactory.withTransaction(session -> productRepository.findById(session, id)
                .map(product->  product.toBuilder().name(productInput.getName())
                        .description(productInput.getDescription())
                        .price(productInput.getPrice())
                        .category(category)
                        .brand(brand)
                        .stocks(stocks)
                        .productImages(productImages)
                        .build())
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
    public Mono<Void> deleteById(UUID id) {
//        return Mono.fromCompletionStage(
//                sessionFactory.withTransaction((session, transaction) -> {
//                    log.info("Deleting product {}", id);
//                    return productRepository.deleteById(session, id);
//                }).thenAccept((e) -> {
//                    this.cacheManager.getCache(PRODUCT_CACHE).clear();
//                }));
        return Mono.empty();
    }

    @Override
    public Mono<Void> rollback(UUID id){
        return sessionFactory.withTransaction(session -> productRepository.findById(session, id)
                .chain(product-> productRepository.delete(session, product)))
                .convert().with(UniReactorConverters.toMono());
    }
}
