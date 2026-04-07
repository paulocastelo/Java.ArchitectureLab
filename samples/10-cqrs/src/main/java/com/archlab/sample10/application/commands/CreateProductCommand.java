package com.archlab.sample10.application.commands;

import com.archlab.sample10.cqrs.Command;
import java.math.BigDecimal;

public record CreateProductCommand(String name, String sku, BigDecimal unitPrice) implements Command {
}
