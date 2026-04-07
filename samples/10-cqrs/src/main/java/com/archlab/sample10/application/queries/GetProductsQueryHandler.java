package com.archlab.sample10.application.queries;

import com.archlab.sample10.application.contracts.ProductResponse;
import com.archlab.sample10.cqrs.QueryHandler;
import com.archlab.sample10.infrastructure.InMemoryProductRepository;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class GetProductsQueryHandler implements QueryHandler<GetProductsQuery, List<ProductResponse>> {
    private final InMemoryProductRepository repository;

    public GetProductsQueryHandler(InMemoryProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ProductResponse> handle(GetProductsQuery query) {
        return repository.findAll().stream()
                .map(product -> new ProductResponse(product.getId(), product.getName(), product.getSku(), product.getUnitPrice()))
                .toList();
    }
}
