package com.archlab.sample12.infrastructure;

import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class OutboxPublisher {
    private static final Logger log = LoggerFactory.getLogger(OutboxPublisher.class);
    private final OutboxRepository outboxRepository;

    public OutboxPublisher(OutboxRepository outboxRepository) {
        this.outboxRepository = outboxRepository;
    }

    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void processOutbox() {
        var pending = outboxRepository.findByProcessedAtUtcIsNullOrderByCreatedAtUtcAsc(PageRequest.of(0, 10));
        for (var message : pending) {
            log.info("Publishing {}: {}", message.getEventType(), message.getPayload());
            message.setProcessedAtUtc(Instant.now());
            outboxRepository.save(message);
        }
    }
}
