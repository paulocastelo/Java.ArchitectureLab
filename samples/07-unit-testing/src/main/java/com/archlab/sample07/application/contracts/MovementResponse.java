package com.archlab.sample07.application.contracts;

import com.archlab.sample07.domain.MovementType;

public record MovementResponse(String productId, MovementType movementType, int quantity, int currentBalance) {
}
