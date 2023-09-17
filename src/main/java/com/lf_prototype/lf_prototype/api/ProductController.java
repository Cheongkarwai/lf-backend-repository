package com.lf_prototype.lf_prototype.api;

import com.lf_prototype.lf_prototype.domain.Product;
import com.lf_prototype.lf_prototype.service.IProductService;
import com.lf_prototype.lf_prototype.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/products")
@CrossOrigin(origins = "http://localhost:4200")
public class ProductController {

    private IProductService productService;

    public ProductController(IProductService productService){
        this.productService = productService;
    }

    @GetMapping
    public Flux<Product> findAll(Pageable pageable){
        return productService.findAll(pageable);
    }
}
