package com.project.domain.classification;

import java.time.OffsetDateTime;

public record EntityMetadata(
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt,
    Long revision
)
{}
