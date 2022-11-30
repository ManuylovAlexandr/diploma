package com.project.domain.event;

import java.time.OffsetDateTime;

public record DomainEventMetadata(
    OffsetDateTime occurredOn,
    Long revision
) {}
