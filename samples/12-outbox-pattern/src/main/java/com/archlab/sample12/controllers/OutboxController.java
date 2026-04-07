package com.archlab.sample12.controllers;

import com.archlab.sample12.infrastructure.OutboxMessage;
import com.archlab.sample12.infrastructure.OutboxRepository;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/outbox")
public class OutboxController {
    private final OutboxRepository outboxRepository;

    public OutboxController(OutboxRepository outboxRepository) {
        this.outboxRepository = outboxRepository;
    }

    @GetMapping
    public List<OutboxMessage> getAll() {
        return outboxRepository.findAll(Sort.by(Sort.Direction.ASC, "createdAtUtc"));
    }
}
