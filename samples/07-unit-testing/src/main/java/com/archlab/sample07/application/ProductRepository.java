package com.archlab.sample07.application;

import com.archlab.sample07.domain.Product;
import com.archlab.sample07.domain.StockMovement;
import java.util.Optional;

public interface ProductRepository {
    Optional<Product> findById(String id);
    Product save(Product product);
    void addMovement(StockMovement movement);
}
