package com.archlab.sample11.application;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductResponse(String id, String name, String sku, BigDecimal unitPrice) {
    public static ProductResponse create(String name, String sku, BigDecimal unitPrice) {
        return new ProductResponse(UUID.randomUUID().toString(), name, sku, unitPrice);
    }
}
