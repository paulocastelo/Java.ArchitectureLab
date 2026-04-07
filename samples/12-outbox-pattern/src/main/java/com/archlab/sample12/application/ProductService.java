package com.archlab.sample12.application;

import com.archlab.sample12.domain.Product;
import com.archlab.sample12.infrastructure.OutboxMessage;
import com.archlab.sample12.infrastructure.OutboxRepository;
import com.archlab.sample12.infrastructure.ProductJpaRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {
    private final ProductJpaRepository productRepository;
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    public ProductService(ProductJpaRepository productRepository, OutboxRepository outboxRepository, ObjectMapper objectMapper) {
        this.productRepository = productRepository;
        this.outboxRepository = outboxRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public ProductResponse create(CreateProductRequest request) {
        var product = new Product();
        product.setName(request.name());
        product.setSku(request.sku());
        product.setUnitPrice(request.unitPrice());
        var saved = productRepository.save(product);

        var outboxMessage = new OutboxMessage();
        outboxMessage.setEventType("ProductCreated");
        outboxMessage.setPayload(toPayload(saved));
        outboxMessage.setCreatedAtUtc(Instant.now());
        outboxRepository.save(outboxMessage);

        return new ProductResponse(saved.getId(), saved.getName(), saved.getSku(), saved.getUnitPrice());
    }

    private String toPayload(Product product) {
        try {
            return objectMapper.writeValueAsString(Map.of("id", product.getId(), "name", product.getName()));
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("Could not serialize outbox payload.", ex);
        }
    }
}
