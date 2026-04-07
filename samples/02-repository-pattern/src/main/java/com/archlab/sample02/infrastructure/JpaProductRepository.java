package com.archlab.sample02.infrastructure;

import com.archlab.sample02.application.ProductRepository;
import com.archlab.sample02.domain.Product;
import java.util.List;
import java.util.Optional;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository
@Profile("jpa")
public class JpaProductRepository implements ProductRepository {
    private final AppJpaRepository jpaRepository;

    public JpaProductRepository(AppJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<Product> findAll() {
        return jpaRepository.findAll();
    }

    @Override
    public Optional<Product> findById(String id) {
        return jpaRepository.findById(id);
    }

    @Override
    public Product save(Product product) {
        return jpaRepository.save(product);
    }
}
