package com.archlab.sample03.application.contracts;

import java.math.BigDecimal;

public record ProductResponse(String id, String name, String sku, BigDecimal unitPrice) {
}
