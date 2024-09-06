package com.lfhardware.product.api;

import com.lfhardware.file.service.FileService;
import com.lfhardware.product.domain.Product;
import com.lfhardware.product.dto.*;
import com.lfhardware.product.service.IProductService;
import com.lfhardware.shared.Search;
import com.lfhardware.shared.Sort;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@Component
public class ProductApi {

    private final IProductService productService;

    private final FileService fileService;

    public ProductApi(IProductService productService, @Qualifier("PDFService") FileService fileService) {
        this.productService = productService;
        this.fileService = fileService;
    }

    /**
     * @param serverRequest - request object
     * @return Mono<ServerResponse>
     *
     */
    public Mono<ServerResponse> findAll(ServerRequest request) {

        Search search = null;

        if(!request.queryParam("keyword").isEmpty()){
            search = new Search(request.queryParams().get("search"), request.queryParam("keyword").orElse(""));
        }

        ProductPageRequest productPageRequest = new ProductPageRequest(
                Integer.parseInt(request.queryParam("page_size").orElse("3")),
                Integer.parseInt(request.queryParam("page").orElse("0")),
                new Sort(request.queryParam("sort").orElse("")),
                search);

        if (request.queryParams().get("brands") != null && !request.queryParams().get("brands").isEmpty()) {
            productPageRequest.setBrandIds(request.queryParams().get("brands").stream().map(Long::valueOf).collect(Collectors.toSet()));
        }

        if (request.queryParams().get("categories") != null && !request.queryParams().get("categories").isEmpty()) {
            productPageRequest.setCategoryIds(request.queryParams().get("categories").stream().map(Long::valueOf).collect(Collectors.toSet()));
        }

        if (request.queryParam("min_quantity").isPresent()) {
            productPageRequest.setMinQuantity(Integer.parseInt(request.queryParam("min_quantity").get()));
        }

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(productService.findAll(productPageRequest), Product.class);
    }

    /**
     * @param serverRequest - request object
     * @return Mono<ServerResponse>
     *
     */
    public Mono<ServerResponse> findById(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(productService.findById(Long.valueOf(request.pathVariable("id"))), Product.class);
    }

    /**
     * @param serverRequest - request object
     * @return Mono<ServerResponse>
     *
     */
    public Mono<ServerResponse> findByName(ServerRequest serverRequest) {
        return productService.findByName(serverRequest.pathVariable("name"))
                .flatMap(productDTO -> ServerResponse.ok().bodyValue(productDTO))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    /**
     * @param serverRequest - request object
     * @return Mono<ServerResponse>
     *
     */
    public Mono<ServerResponse> save(ServerRequest request) {
        return request.bodyToMono(ProductInput.class).flatMap(productService::save)
                .then(ServerResponse.ok().build());
    }

    /**
     * @param serverRequest - request object
     * @return Mono<ServerResponse>
     *
     */
    public Mono<ServerResponse> updateById(ServerRequest request) {
        return request.bodyToMono(ProductInput.class).flatMap(product->productService.updateById(Long.valueOf(request.pathVariable("id")),product)
                .then(ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).build()));
    }

    /**
     * @param serverRequest - request object
     * @return Mono<ServerResponse>
     *
     */
    public Mono<ServerResponse> findAllProductCategory(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .body(productService.findAllProductCategory(), CategoryDTO.class);
    }

    /**
     * @param serverRequest - request object
     * @return Mono<ServerResponse>
     *
     */
    public Mono<ServerResponse> findAllProductBrand(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .body(productService.findAllProductBrand(), BrandDTO.class);
    }

    /**
     * @param serverRequest - request object
     * @return Mono<ServerResponse>
     *
     */
    public Mono<ServerResponse> deleteById(ServerRequest serverRequest) {
        return productService.deleteById(Long.valueOf(serverRequest.pathVariable("id")))
                .then(ServerResponse.ok().build());
    }

}
