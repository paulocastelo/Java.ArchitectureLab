package com.archlab.sample06.application;

import java.math.BigDecimal;

public record ProductResponse(String id, String name, String sku, BigDecimal unitPrice) {
}
