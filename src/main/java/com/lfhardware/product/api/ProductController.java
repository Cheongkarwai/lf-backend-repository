package com.lfhardware.product.api;

import com.lfhardware.file.service.FileService;
import com.lfhardware.product.domain.Product;
import com.lfhardware.product.dto.*;
import com.lfhardware.product.service.IProductService;
import com.lfhardware.core.repository.Search;
import com.lfhardware.core.repository.Sort;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final IProductService productService;

    private final FileService fileService;

    public ProductController(IProductService productService, @Qualifier("PDFService") FileService fileService) {
        this.productService = productService;
        this.fileService = fileService;
    }


    @GetMapping
    public Flux<ProductDTO> findAll(@RequestParam(defaultValue = "20") int pageSize,
                                    @RequestParam(defaultValue = "0") int pageNumber,
                                    @RequestParam String sort,
                                    ServerWebExchange serverWebExchange) {
        ProductPageRequest productPageRequest = new ProductPageRequest(
                pageSize,
                pageNumber,
                new Sort(sort),
                null);

        return productService.count(productPageRequest)
                .flatMapMany(count-> {
                    serverWebExchange.getResponse().getHeaders().add("X-Total-Count", String.valueOf(count));
                    serverWebExchange.getResponse().getHeaders().add("X-Page-Number", String.valueOf(pageNumber));
                    serverWebExchange.getResponse().getHeaders().add("X-Page-Size", String.valueOf(pageSize));
                    serverWebExchange.getResponse().getHeaders().add("X-Total-Pages", String.valueOf((int) Math.ceil((double) count / pageSize)));
                    return productService.findAll(productPageRequest);
                });
    }

    @GetMapping("/{id}")
    public Mono<ProductDTO> findById(@PathVariable UUID id) {
        return productService.findById(id);
    }

    public Mono<ServerResponse> findByName(ServerRequest serverRequest) {
        return productService.findByName(serverRequest.pathVariable("name"))
                .flatMap(productDTO -> ServerResponse.ok().bodyValue(productDTO))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    @PostMapping
    public Mono<ProductDTO> save(@RequestBody Mono<ProductInput> productInputMono) {
        return productInputMono.flatMap(productService::save);
    }

    @PutMapping("/{id}")
    public Mono<ServerResponse> updateById(@PathVariable UUID id, @RequestBody Mono<ProductInput> productInputMono) {
        return productInputMono.flatMap(product-> productService.updateById(id,product)
                .then(ServerResponse.ok().build()));
    }

    public Mono<ServerResponse> findAllProductCategory(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .body(productService.findAllProductCategory(), CategoryDTO.class);
    }

    public Mono<ServerResponse> findAllProductBrand(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .body(productService.findAllProductBrand(), BrandDTO.class);
    }

    public Mono<ServerResponse> deleteById(ServerRequest serverRequest) {
        return productService.deleteById(UUID.fromString(serverRequest.pathVariable("id")))
                .then(ServerResponse.ok().build());
    }

}
