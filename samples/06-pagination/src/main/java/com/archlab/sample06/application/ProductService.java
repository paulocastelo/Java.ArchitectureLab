package com.archlab.sample06.application;

import com.archlab.sample06.common.PagedResult;
import com.archlab.sample06.infrastructure.ProductJpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    private final ProductJpaRepository repository;

    public ProductService(ProductJpaRepository repository) {
        this.repository = repository;
    }

    public PagedResult<ProductResponse> findAll(Pageable pageable) {
        var page = repository.findAll(pageable)
                .map(product -> new ProductResponse(product.getId(), product.getName(), product.getSku(), product.getUnitPrice()));
        return PagedResult.from(page);
    }
}
