package com.archlab.sample03.controllers;

import com.archlab.sample03.application.ProductService;
import com.archlab.sample03.application.contracts.CreateProductRequest;
import com.archlab.sample03.application.contracts.ProductResponse;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductsController {
    private final ProductService productService;

    public ProductsController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/mapstruct")
    public List<ProductResponse> getWithMapStruct() {
        return productService.getWithMapStruct();
    }

    @GetMapping("/manual")
    public List<ProductResponse> getManual() {
        return productService.getManual();
    }

    @PostMapping
    public ResponseEntity<ProductResponse> create(@RequestBody CreateProductRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.create(request));
    }
}
