package com.archlab.sample10.controllers;

import com.archlab.sample10.application.commands.CreateProductCommand;
import com.archlab.sample10.application.contracts.CreateProductRequest;
import com.archlab.sample10.application.contracts.ProductResponse;
import com.archlab.sample10.application.queries.GetProductByIdQuery;
import com.archlab.sample10.application.queries.GetProductsQuery;
import com.archlab.sample10.cqrs.CommandBus;
import com.archlab.sample10.cqrs.QueryBus;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductsController {
    private final CommandBus commandBus;
    private final QueryBus queryBus;

    public ProductsController(CommandBus commandBus, QueryBus queryBus) {
        this.commandBus = commandBus;
        this.queryBus = queryBus;
    }

    @GetMapping
    public List<ProductResponse> getAll() {
        return queryBus.send(new GetProductsQuery());
    }

    @GetMapping("/{id}")
    public ProductResponse getById(@PathVariable String id) {
        return queryBus.send(new GetProductByIdQuery(id));
    }

    @PostMapping
    public ResponseEntity<ProductResponse> create(@RequestBody @Valid CreateProductRequest request) {
        ProductResponse result = commandBus.send(new CreateProductCommand(request.name(), request.sku(), request.unitPrice()));
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
}
