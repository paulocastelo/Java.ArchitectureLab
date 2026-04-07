package com.archlab.sample10.application.queries;

import com.archlab.sample10.application.contracts.ProductResponse;
import com.archlab.sample10.cqrs.QueryHandler;
import com.archlab.sample10.infrastructure.InMemoryProductRepository;
import org.springframework.stereotype.Component;

@Component
public class GetProductByIdQueryHandler implements QueryHandler<GetProductByIdQuery, ProductResponse> {
    private final InMemoryProductRepository repository;

    public GetProductByIdQueryHandler(InMemoryProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public ProductResponse handle(GetProductByIdQuery query) {
        var product = repository.findById(query.id())
                .orElseThrow(() -> new IllegalArgumentException("Product not found."));
        return new ProductResponse(product.getId(), product.getName(), product.getSku(), product.getUnitPrice());
    }
}
