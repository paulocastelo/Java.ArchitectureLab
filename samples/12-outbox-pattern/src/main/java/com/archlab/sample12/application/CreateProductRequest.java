package com.archlab.sample12.application;

import java.math.BigDecimal;

public record CreateProductRequest(String name, String sku, BigDecimal unitPrice) {
}
