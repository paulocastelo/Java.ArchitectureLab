package com.archlab.sample07.application.contracts;

import com.archlab.sample07.domain.MovementType;

public record CreateMovementRequest(String productId, MovementType movementType, int quantity) {
}
