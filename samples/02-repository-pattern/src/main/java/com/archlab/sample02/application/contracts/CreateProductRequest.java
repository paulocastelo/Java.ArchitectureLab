package com.archlab.sample02.application.contracts;

import java.math.BigDecimal;

public record CreateProductRequest(String name, String sku, BigDecimal unitPrice) {
}
