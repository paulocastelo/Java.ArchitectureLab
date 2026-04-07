package com.archlab.sample10.application.commands;

import com.archlab.sample10.application.contracts.ProductResponse;
import com.archlab.sample10.cqrs.CommandHandler;
import com.archlab.sample10.domain.Product;
import com.archlab.sample10.infrastructure.InMemoryProductRepository;
import org.springframework.stereotype.Component;

@Component
public class CreateProductCommandHandler implements CommandHandler<CreateProductCommand, ProductResponse> {
    private final InMemoryProductRepository repository;

    public CreateProductCommandHandler(InMemoryProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public ProductResponse handle(CreateProductCommand command) {
        var product = new Product();
        product.setName(command.name());
        product.setSku(command.sku());
        product.setUnitPrice(command.unitPrice());
        var saved = repository.save(product);
        return new ProductResponse(saved.getId(), saved.getName(), saved.getSku(), saved.getUnitPrice());
    }
}
