package com.archlab.sample02.application;

import com.archlab.sample02.application.contracts.CreateProductRequest;
import com.archlab.sample02.application.contracts.ProductResponse;
import com.archlab.sample02.domain.Product;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductResponse> findAll() {
        return productRepository.findAll().stream()
                .map(product -> new ProductResponse(product.getId(), product.getName(), product.getSku(), product.getUnitPrice()))
                .toList();
    }

    @Transactional
    public ProductResponse create(CreateProductRequest request) {
        var product = new Product();
        product.setName(request.name());
        product.setSku(request.sku());
        product.setUnitPrice(request.unitPrice());

        var saved = productRepository.save(product);
        return new ProductResponse(saved.getId(), saved.getName(), saved.getSku(), saved.getUnitPrice());
    }
}
