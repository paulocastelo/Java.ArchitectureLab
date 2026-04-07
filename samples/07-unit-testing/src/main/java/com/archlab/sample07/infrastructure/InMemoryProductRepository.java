package com.archlab.sample07.infrastructure;

import com.archlab.sample07.application.ProductRepository;
import com.archlab.sample07.domain.Product;
import com.archlab.sample07.domain.StockMovement;
import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryProductRepository implements ProductRepository {
    private final Map<String, Product> store = new ConcurrentHashMap<>();
    private final List<StockMovement> movements = new ArrayList<>();

    @PostConstruct
    void seed() {
        var product = new Product();
        product.setId("sample-product");
        product.setName("Keyboard");
        product.setCurrentBalance(10);
        store.put(product.getId(), product);
    }

    @Override
    public Optional<Product> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Product save(Product product) {
        store.put(product.getId(), product);
        return product;
    }

    @Override
    public void addMovement(StockMovement movement) {
        movements.add(movement);
    }
}
