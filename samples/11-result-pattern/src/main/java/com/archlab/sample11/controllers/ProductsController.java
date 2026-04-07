package com.archlab.sample11.controllers;

import com.archlab.sample11.application.CreateProductRequest;
import com.archlab.sample11.application.ProductResponse;
import com.archlab.sample11.application.ProductService;
import com.archlab.sample11.common.sealed.Failure;
import com.archlab.sample11.common.sealed.Success;
import java.util.List;
import java.util.Map;
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

    @GetMapping
    public List<ProductResponse> getAll() {
        return productService.getAll();
    }

    @PostMapping("/class")
    public ResponseEntity<?> createWithClass(@RequestBody CreateProductRequest request) {
        var result = productService.create(request);
        if (result.isSuccess()) {
            return ResponseEntity.ok(result.getValue());
        }

        return switch (result.getErrorCode()) {
            case "sku_conflict" -> ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("detail", result.getError()));
            default -> ResponseEntity.badRequest().body(Map.of("detail", result.getError()));
        };
    }

    @PostMapping("/sealed")
    public ResponseEntity<?> createWithSealed(@RequestBody CreateProductRequest request) {
        var result = productService.createSealed(request);
        return switch (result) {
            case Success<ProductResponse> success -> ResponseEntity.ok(success.value());
            case Failure<ProductResponse> failure -> switch (failure.errorCode()) {
                case "sku_conflict" -> ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("detail", failure.error()));
                default -> ResponseEntity.badRequest().body(Map.of("detail", failure.error()));
            };
        };
    }
}
