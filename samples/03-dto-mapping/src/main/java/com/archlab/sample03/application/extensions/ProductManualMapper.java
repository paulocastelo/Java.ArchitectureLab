package com.archlab.sample03.application.extensions;

import com.archlab.sample03.application.contracts.ProductResponse;
import com.archlab.sample03.domain.Product;

public final class ProductManualMapper {
    private ProductManualMapper() {
    }

    public static ProductResponse toResponse(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getSku(), product.getUnitPrice());
    }
}
