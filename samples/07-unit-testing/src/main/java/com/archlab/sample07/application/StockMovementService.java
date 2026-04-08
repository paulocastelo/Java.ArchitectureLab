package com.archlab.sample07.application;

import com.archlab.sample07.application.contracts.CreateMovementRequest;
import com.archlab.sample07.application.contracts.MovementResponse;
import com.archlab.sample07.domain.StockMovement;
import org.springframework.stereotype.Service;

@Service
public class StockMovementService {
    private final ProductRepository productRepository;

    public StockMovementService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public MovementResponse create(CreateMovementRequest request) {
        if (request.quantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }

        var product = productRepository.findById(request.productId())
                .orElseThrow(() -> new NotFoundException("Product not found."));

        int newBalance = switch (request.movementType()) {
            case ENTRY -> product.getCurrentBalance() + request.quantity();
            case EXIT -> {
                if (product.getCurrentBalance() < request.quantity()) {
                    throw new IllegalStateException("Insufficient stock balance.");
                }
                yield product.getCurrentBalance() - request.quantity();
            }
        };

        product.setCurrentBalance(newBalance);

        var movement = new StockMovement();
        movement.setProductId(product.getId());
        movement.setMovementType(request.movementType());
        movement.setQuantity(request.quantity());

        productRepository.addMovement(movement);
        productRepository.save(product);

        return new MovementResponse(product.getId(), request.movementType(), request.quantity(), newBalance);
    }
}
