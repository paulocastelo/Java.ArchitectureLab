package com.archlab.sample07.domain;

import java.time.Instant;

public class StockMovement {
    private String productId;
    private MovementType movementType;
    private int quantity;
    private Instant occurredAt = Instant.now();

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public MovementType getMovementType() { return movementType; }
    public void setMovementType(MovementType movementType) { this.movementType = movementType; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public Instant getOccurredAt() { return occurredAt; }
    public void setOccurredAt(Instant occurredAt) { this.occurredAt = occurredAt; }
}
