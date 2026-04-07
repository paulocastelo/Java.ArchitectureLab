package com.archlab.sample06.infrastructure;

import com.archlab.sample06.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductJpaRepository extends JpaRepository<Product, String> {
}
