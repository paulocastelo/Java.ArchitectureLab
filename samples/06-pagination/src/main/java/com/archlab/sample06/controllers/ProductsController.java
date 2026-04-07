package com.archlab.sample06.controllers;

import com.archlab.sample06.application.ProductService;
import com.archlab.sample06.application.ProductResponse;
import com.archlab.sample06.common.PagedResult;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
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
    public PagedResult<ProductResponse> getAll(@PageableDefault(size = 10) Pageable pageable) {
        return productService.findAll(pageable);
    }
}
