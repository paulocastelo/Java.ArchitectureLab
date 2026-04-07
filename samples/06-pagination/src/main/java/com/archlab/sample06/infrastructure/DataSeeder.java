package com.archlab.sample06.infrastructure;

import com.archlab.sample06.domain.Product;
import java.math.BigDecimal;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements ApplicationRunner {
    private final ProductJpaRepository repository;

    public DataSeeder(ProductJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (repository.count() > 0) {
            return;
        }

        for (int index = 1; index <= 100; index++) {
            var product = new Product();
            product.setName("Product " + index);
            product.setSku(String.format("PAG-%03d", index));
            product.setUnitPrice(new BigDecimal("10.00").add(BigDecimal.valueOf(index)));
            repository.save(product);
        }
    }
}
