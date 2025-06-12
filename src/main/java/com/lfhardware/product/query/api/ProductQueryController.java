package com.lfhardware.product.query.api;

import com.lfhardware.core.dto.PageRequest;
import com.lfhardware.product.dto.ProductDTO;
import com.lfhardware.product.dto.ProductSearchRequest;
import com.lfhardware.product.query.query.ProductByIdQuery;
import com.lfhardware.product.query.query.ProductQueryCountAll;
import com.lfhardware.product.query.query.ProductSearchQuery;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.extensions.reactor.queryhandling.gateway.ReactorQueryGateway;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/products")
public class ProductQueryController {

    private final ReactorQueryGateway reactorQueryGateway;

    public ProductQueryController(ReactorQueryGateway reactorQueryGateway) {
        this.reactorQueryGateway = reactorQueryGateway;
    }


    @Operation(summary = "Get product by id")
    @ApiResponse(
            responseCode = "200",
            useReturnTypeSchema = true,
            description = "Successful response",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @GetMapping("/{id}")
    public Mono<ProductDTO> findById(@PathVariable String id) {
        ProductByIdQuery productByIdQuery = new ProductByIdQuery(id);
        return reactorQueryGateway.query(productByIdQuery, ProductDTO.class);
    }

    @Operation(summary = "Get all products")
    @ApiResponse(
            responseCode = "200",
            useReturnTypeSchema = true,
            description = "Successful response",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))

    @GetMapping(produces = {MediaType.APPLICATION_NDJSON_VALUE})
    public Flux<ProductDTO> findAll(PageRequest pageRequest,
                                    ServerWebExchange serverWebExchange) {
        ProductSearchQuery productSearchQuery = new ProductSearchQuery(pageRequest, null, null);
        ProductQueryCountAll productQueryCountAll = new ProductQueryCountAll(null, null);
        return reactorQueryGateway.query(productQueryCountAll, Long.class)
                .flatMapMany(count -> {
                    setPaginationMetadata(pageRequest, serverWebExchange, count);
                    return reactorQueryGateway.streamingQuery(productSearchQuery, ProductDTO.class);
                });
    }


    @Operation(summary = "Search products")
    @ApiResponse(
            responseCode = "200",
            useReturnTypeSchema = true,
            description = "Successful response",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @PostMapping(value = ":search", produces = {MediaType.APPLICATION_NDJSON_VALUE})
    public Flux<ProductDTO> search(PageRequest pageRequest,
                                   @RequestBody ProductSearchRequest productSearchRequest,
                                   ServerWebExchange serverWebExchange) {
        ProductSearchQuery productQueryAll = new ProductSearchQuery(pageRequest, productSearchRequest.getSearch(), productSearchRequest.getFilter());
        ProductQueryCountAll productQueryCountAll = new ProductQueryCountAll(productSearchRequest.getSearch(), productSearchRequest.getFilter());
        return reactorQueryGateway.query(productQueryCountAll, Long.class)
                .flatMapMany(count -> {
                    setPaginationMetadata(pageRequest, serverWebExchange, count);
                    return reactorQueryGateway.streamingQuery(productQueryAll, ProductDTO.class);
                });
    }

    public static void setPaginationMetadata(PageRequest pageRequest, ServerWebExchange serverWebExchange, Long count) {
        serverWebExchange.getResponse().getHeaders().add("X-Total-Count", String.valueOf(count));
        serverWebExchange.getResponse().getHeaders().add("X-Page-Number", String.valueOf(pageRequest.getPageNo()));
        serverWebExchange.getResponse().getHeaders().add("X-Page-Size", String.valueOf(pageRequest.getPageSize()));
        serverWebExchange.getResponse().getHeaders().add("X-Total-Pages", String.valueOf((int) Math.ceil((double) count / pageRequest.getPageSize())));
    }
}
