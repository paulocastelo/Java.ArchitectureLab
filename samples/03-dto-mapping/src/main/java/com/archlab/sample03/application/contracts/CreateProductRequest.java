package com.archlab.sample03.application.contracts;

import java.math.BigDecimal;

public record CreateProductRequest(String name, String sku, BigDecimal unitPrice) {
}
