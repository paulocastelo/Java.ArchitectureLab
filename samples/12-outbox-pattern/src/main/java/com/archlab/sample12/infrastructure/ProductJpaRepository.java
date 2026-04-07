package com.archlab.sample12.infrastructure;

import com.archlab.sample12.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductJpaRepository extends JpaRepository<Product, String> {
}
