package com.archlab.sample09.service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    private static final Logger log = LoggerFactory.getLogger(ProductService.class);

    private final List<ProductResponse> products = List.of(
            new ProductResponse("prod-1", "Notebook", true),
            new ProductResponse("prod-2", "Legacy Printer", false));

    public List<ProductResponse> getAll() {
        log.debug("Fetching all products");
        log.info("Returning {} products", products.size());
        return products;
    }

    public ProductResponse findById(String id) {
        log.debug("Fetching product {}", id);
        var product = products.stream().filter(item -> item.id().equals(id)).findFirst()
                .orElseThrow(() -> {
                    log.error("Product {} was not found", id);
                    return new IllegalArgumentException("Product not found.");
                });

        log.info("Product {} retrieved successfully", id);
        if (!product.active()) {
            log.warn("Product {} is inactive", id);
        }

        return product;
    }
}
