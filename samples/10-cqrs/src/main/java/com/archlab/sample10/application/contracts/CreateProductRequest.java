package com.archlab.sample10.application.contracts;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;

public record CreateProductRequest(
        @NotBlank String name,
        @NotBlank String sku,
        @DecimalMin("0.01") BigDecimal unitPrice) {
}
