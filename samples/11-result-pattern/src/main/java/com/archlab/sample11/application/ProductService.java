package com.archlab.sample11.application;

import com.archlab.sample11.common.Result;
import com.archlab.sample11.common.sealed.Failure;
import com.archlab.sample11.common.sealed.ResultBase;
import com.archlab.sample11.common.sealed.Success;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    private final List<ProductResponse> products = new ArrayList<>(List.of(
            ProductResponse.create("Notebook", "RES-001", java.math.BigDecimal.valueOf(3200))));

    public Result<ProductResponse> create(CreateProductRequest request) {
        if (products.stream().anyMatch(product -> product.sku().equalsIgnoreCase(request.sku()))) {
            return Result.failure("SKU already exists.", "sku_conflict");
        }

        if (request.unitPrice().signum() <= 0) {
            return Result.failure("Unit price must be positive.", "invalid_price");
        }

        var product = ProductResponse.create(request.name().trim(), request.sku().trim(), request.unitPrice());
        products.add(product);
        return Result.success(product);
    }

    public ResultBase<ProductResponse> createSealed(CreateProductRequest request) {
        if (products.stream().anyMatch(product -> product.sku().equalsIgnoreCase(request.sku()))) {
            return new Failure<>("SKU already exists.", "sku_conflict");
        }

        if (request.unitPrice().signum() <= 0) {
            return new Failure<>("Unit price must be positive.", "invalid_price");
        }

        var product = ProductResponse.create(request.name().trim(), request.sku().trim(), request.unitPrice());
        products.add(product);
        return new Success<>(product);
    }

    public List<ProductResponse> getAll() {
        return List.copyOf(products);
    }
}
