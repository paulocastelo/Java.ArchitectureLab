package com.archlab.sample10.application.contracts;

import java.math.BigDecimal;

public record ProductResponse(String id, String name, String sku, BigDecimal unitPrice) {
}
