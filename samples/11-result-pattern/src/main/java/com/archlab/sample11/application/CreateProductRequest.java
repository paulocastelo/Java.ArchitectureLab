package com.archlab.sample11.application;

import java.math.BigDecimal;

public record CreateProductRequest(String name, String sku, BigDecimal unitPrice) {
}
