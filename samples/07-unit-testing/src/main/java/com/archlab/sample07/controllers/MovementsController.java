package com.archlab.sample07.controllers;

import com.archlab.sample07.application.StockMovementService;
import com.archlab.sample07.application.contracts.CreateMovementRequest;
import com.archlab.sample07.application.contracts.MovementResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/movements")
public class MovementsController {
    private final StockMovementService stockMovementService;

    public MovementsController(StockMovementService stockMovementService) {
        this.stockMovementService = stockMovementService;
    }

    @PostMapping
    public ResponseEntity<MovementResponse> create(@RequestBody CreateMovementRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(stockMovementService.create(request));
    }
}
