package com.archlab.sample12.application;

import java.math.BigDecimal;

public record ProductResponse(String id, String name, String sku, BigDecimal unitPrice) {
}
