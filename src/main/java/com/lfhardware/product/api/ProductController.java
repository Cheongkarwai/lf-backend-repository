//package com.lfhardware.product.api;
//
//import com.lfhardware.core.dto.ErrorResponse;
//import com.lfhardware.core.dto.PageRequest;
//import com.lfhardware.core.repository.Search;
//import com.lfhardware.file.service.FileService;
//import com.lfhardware.product.dto.ProductDTO;
//import com.lfhardware.product.dto.ProductFilterCriteria;
//import com.lfhardware.product.dto.ProductInput;
//import com.lfhardware.product.query.api.ProductQueryController;
//import com.lfhardware.product.service.IProductService;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.media.Content;
//import io.swagger.v3.oas.annotations.media.Schema;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.responses.ApiResponses;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.hateoas.server.reactive.WebFluxLinkBuilder;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
//import java.util.UUID;
//
//
//@RestController
//@RequestMapping("/api/v1/products")
//@Slf4j
//public class ProductController {
//
//    private final IProductService productService;
//
//    private final FileService fileService;
//
//
//    public ProductController(IProductService productService, @Qualifier("PDFService") FileService fileService) {
//        this.productService = productService;
//        this.fileService = fileService;
//    }
//
//
//    @Operation(summary = "Get all products")
//    @ApiResponses(value = {
//            @ApiResponse(
//                    responseCode = "200",
//                    useReturnTypeSchema = true,
//                    description = "Successful response",
//                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
//    })
//    @GetMapping
//    public Flux<ProductDTO> findAll(PageRequest pageRequest,
//                                    @Validated Search searchCriteria,
//                                    @Validated ProductFilterCriteria filterCriteria,
//                                    ServerWebExchange serverWebExchange) {
//        return productService.count(searchCriteria, filterCriteria)
//                .flatMapMany(count -> {
//                    ProductQueryController.setPaginationMetadata(pageRequest, serverWebExchange, count);
//                    return productService.findAll(pageRequest, searchCriteria, filterCriteria)
//                            .flatMap(productDTO -> {
//                                return WebFluxLinkBuilder.linkTo(
//                                                WebFluxLinkBuilder.methodOn(ProductController.class)
//                                                        .findById(productDTO.getId()))
//                                        .withSelfRel()
//                                        .toMono()
//                                        .map(productDTO::add);
//                            });
//                });
//    }
//
//    @ApiResponses(value = {
//            @ApiResponse(
//                    responseCode = "200",
//                    description = "Successful response",
//                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
//            @ApiResponse(
//                    responseCode = "404",
//                    description = "Resource Not Found",
//                    content = @Content(
//                            mediaType = MediaType.APPLICATION_JSON_VALUE,
//                            schema = @Schema(implementation = ErrorResponse.class)
//                    )
//            )
//    })
//    @GetMapping("/{id}")
//    public Mono<ResponseEntity<ProductDTO>> findById(@PathVariable UUID id) {
//        return productService.findById(id)
//                .flatMap(productDTO -> WebFluxLinkBuilder.linkTo(
//                                WebFluxLinkBuilder.methodOn(ProductController.class)
//                                        .findById(productDTO.getId()))
//                        .withSelfRel()
//                        .toMono()
//                        .map(productDTO::add))
//                .map(ResponseEntity::ok);
//    }
//
//    @ApiResponses({
//            @ApiResponse(
//                    responseCode = "201",
//                    description = "Product is created",
//                    content = @Content(
//                            mediaType = MediaType.APPLICATION_JSON_VALUE
//                    )
//            )
//    })
//    @PostMapping
//    public Mono<HttpEntity<ProductDTO>> save(@RequestBody Mono<ProductInput> productInputMono, ServerHttpRequest serverHttpRequest) {
//        return productInputMono
//                .flatMap(productService::save)
//                .map(ResponseEntity::ok);
//        //.map(product -> ResponseEntity.created(URI.create(UriComponentsBuilder.fromPath(serverHttpRequest.getPath().value()).build(product.getId()).getPath())).body(product));
//    }
//
//    @ApiResponses({
//            @ApiResponse(
//                    responseCode = "204",
//                    description = "Product is updated",
//                    content = @Content(
//                            mediaType = MediaType.APPLICATION_JSON_VALUE
//                    )
//            )
//    })
//    @PutMapping("/{id}")
//    public Mono<HttpEntity<Void>> updateById(@PathVariable UUID id, @Validated @RequestBody Mono<ProductInput> productInputMono) {
//        return productInputMono.flatMap(product -> productService.updateById(id, product)
//                .then(Mono.fromCallable(() -> ResponseEntity.noContent().build())));
//    }
//
//    @ApiResponses({
//            @ApiResponse(
//                    responseCode = "204",
//                    description = "Product is deleted",
//                    content = @Content(
//                            mediaType = MediaType.APPLICATION_JSON_VALUE
//                    )
//            )
//    })
//    @DeleteMapping("/{id}")
//    public Mono<HttpEntity<Void>> deleteById(@PathVariable UUID id) {
//        return productService.deleteById(id)
//                .then(Mono.fromCallable(() -> ResponseEntity.noContent().build()));
//    }
//}
