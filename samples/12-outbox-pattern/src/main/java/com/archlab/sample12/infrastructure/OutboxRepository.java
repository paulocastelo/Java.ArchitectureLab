package com.archlab.sample12.infrastructure;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutboxRepository extends JpaRepository<OutboxMessage, String> {
    List<OutboxMessage> findByProcessedAtUtcIsNullOrderByCreatedAtUtcAsc(Pageable pageable);
}
