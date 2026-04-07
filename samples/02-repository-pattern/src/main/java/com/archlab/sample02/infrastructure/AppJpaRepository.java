package com.archlab.sample02.infrastructure;

import com.archlab.sample02.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppJpaRepository extends JpaRepository<Product, String> {
}
