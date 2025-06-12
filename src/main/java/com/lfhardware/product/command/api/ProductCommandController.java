package com.lfhardware.product.command.api;

import com.lfhardware.product.command.command.DeleteProductCommand;
import com.lfhardware.product.command.command.UpdateProductCommand;
import com.lfhardware.product.command.dto.CreateProductInput;
import com.lfhardware.product.command.dto.UpdateProductInput;
import com.lfhardware.product.mapper.ProductMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.axonframework.extensions.reactor.commandhandling.gateway.ReactorCommandGateway;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequestMapping("/api/products")
public class ProductCommandController {

    public ReactorCommandGateway reactorCommandGateway;

    private final ProductMapper productMapper;

    public ProductCommandController(ReactorCommandGateway reactorCommandGateway,
                                    ProductMapper productMapper) {
        this.reactorCommandGateway = reactorCommandGateway;
        this.productMapper = productMapper;
    }


    @Operation(summary = "Create product")
    @ApiResponse(
            responseCode = "201",
            useReturnTypeSchema = true,
            description = "Successful response",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @PostMapping
    public Mono<HttpEntity<Void>> create(@Valid @RequestBody Mono<CreateProductInput> createProductInputMono,
                                         ServerWebExchange serverWebExchange) {
        return createProductInputMono
                .map(productMapper::mapToCreateProductCommand)
                .flatMap(createProductCommand -> reactorCommandGateway.send(createProductCommand))
                .map(id -> ResponseEntity.created(URI.create(
                                UriComponentsBuilder.fromPath(
                                                serverWebExchange.getRequest()
                                                        .getPath().value())
                                        .build(id)
                                        .getPath()))
                        .build());
    }


    @Operation(summary = "Update product")
    @ApiResponse(
            responseCode = "200",
            useReturnTypeSchema = true,
            description = "Successful response",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    @PutMapping("/{id}")
    public Mono<String> update(@PathVariable String id, @Valid @RequestBody Mono<UpdateProductInput> updateProductInputMono) {
        return updateProductInputMono
                .map(updateProductInput -> {
                    UpdateProductCommand updateProductCommand = productMapper.mapToUpdateProductCommand(updateProductInput);
                    updateProductCommand.setId(id);
                    return updateProductCommand;
                })
                .flatMap(updateProductCommand -> reactorCommandGateway.send(updateProductCommand));
    }

    @DeleteMapping("/{id}")
    public Mono<String> delete(@PathVariable String id) {
        return reactorCommandGateway.send(new DeleteProductCommand(id));
    }
}
