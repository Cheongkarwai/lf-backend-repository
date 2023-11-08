package com.lfhardware.product.handler;

import com.lfhardware.product.ProductForm;
import com.lfhardware.product.domain.Product;
import com.lfhardware.product.service.IProductService;
import com.lfhardware.shared.PageInfo;
import com.lfhardware.shared.Sort;
import com.lfhardware.shared.SortOrder;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class ProductHandler {

    private final IProductService productService;

    public ProductHandler(IProductService productService){
        this.productService = productService;
    }

    public Mono<ServerResponse> findAll(ServerRequest request){
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(productService.findAll(PageInfo.builder()
                                .page(Integer.parseInt(request.queryParam("page").orElse("0")))
                                .pageSize(Integer.parseInt(request.queryParam("page_size").orElse("3")))
                        .build()),Product.class);
    }

    public Mono<ServerResponse> findById(ServerRequest request){
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(productService.findById(Long.valueOf(request.pathVariable("id"))),Product.class);
    }

    public Mono<ServerResponse> save(ServerRequest request){
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(request.bodyToMono(Product.class).flatMap(productService::save),Product.class);
    }

    public Mono<ServerResponse> updateById(ServerRequest request){
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(request.bodyToMono(ProductForm.class)
                        .flatMap(product->productService.updateById(Long.valueOf(request.pathVariable("id")),product)),Product.class);
    }
}
