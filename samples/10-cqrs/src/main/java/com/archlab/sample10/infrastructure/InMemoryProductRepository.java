package com.archlab.sample10.infrastructure;

import com.archlab.sample10.domain.Product;
import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class InMemoryProductRepository {
    private final Map<String, Product> store = new ConcurrentHashMap<>();

    @PostConstruct
    void seed() {
        var product = new Product();
        product.setName("Seed Product");
        product.setSku("CQRS-001");
        product.setUnitPrice(new BigDecimal("99.90"));
        store.put(product.getId(), product);
    }

    public Product save(Product product) {
        store.put(product.getId(), product);
        return product;
    }

    public Optional<Product> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    public List<Product> findAll() {
        return new ArrayList<>(store.values());
    }
}
