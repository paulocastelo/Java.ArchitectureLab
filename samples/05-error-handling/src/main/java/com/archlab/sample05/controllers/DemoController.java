package com.archlab.sample05.controllers;

import com.archlab.sample05.exceptions.BusinessRuleException;
import com.archlab.sample05.exceptions.ConflictException;
import com.archlab.sample05.exceptions.NotFoundException;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/demo")
public class DemoController {
    @GetMapping("/ok")
    public Map<String, Object> ok() {
        return Map.of("status", "ok", "message", "No exception thrown.");
    }

    @GetMapping("/not-found")
    public void notFound() {
        throw new NotFoundException("Product with id 'abc-123' was not found.");
    }

    @GetMapping("/conflict")
    public void conflict() {
        throw new ConflictException("A product with this SKU already exists.");
    }

    @GetMapping("/business")
    public void business() {
        throw new BusinessRuleException("Inactive products cannot be sold.");
    }

    @GetMapping("/unexpected")
    public void unexpected() {
        throw new IllegalStateException("Unexpected failure for demo purposes.");
    }
}
