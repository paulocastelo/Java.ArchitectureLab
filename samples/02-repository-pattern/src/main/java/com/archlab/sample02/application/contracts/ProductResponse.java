package com.archlab.sample02.application.contracts;

import java.math.BigDecimal;

public record ProductResponse(String id, String name, String sku, BigDecimal unitPrice) {
}
