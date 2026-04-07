package com.archlab.sample03.application;

import com.archlab.sample03.application.contracts.CreateProductRequest;
import com.archlab.sample03.application.contracts.ProductResponse;
import com.archlab.sample03.application.extensions.ProductManualMapper;
import com.archlab.sample03.application.mappings.ProductMapper;
import com.archlab.sample03.domain.Product;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    private final ProductMapper productMapper;
    private final List<Product> products = new ArrayList<>();

    public ProductService(ProductMapper productMapper) {
        this.productMapper = productMapper;

        var seed = new Product();
        seed.setName("Notebook Pro");
        seed.setSku("MAP-001");
        seed.setUnitPrice(new BigDecimal("5999.90"));
        seed.setInternalCost(new BigDecimal("4200.00"));
        seed.setInternalNotes("Supplier discount margin");
        products.add(seed);
    }

    public List<ProductResponse> getWithMapStruct() {
        return products.stream().map(productMapper::toResponse).toList();
    }

    public List<ProductResponse> getManual() {
        return products.stream().map(ProductManualMapper::toResponse).toList();
    }

    public ProductResponse create(CreateProductRequest request) {
        var product = new Product();
        product.setName(request.name());
        product.setSku(request.sku());
        product.setUnitPrice(request.unitPrice());
        product.setInternalCost(request.unitPrice().multiply(new BigDecimal("0.8")));
        product.setInternalNotes("Created via DTO sample");
        products.add(product);
        return productMapper.toResponse(product);
    }
}
