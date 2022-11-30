package com.project.domain.event.deserializers;

import javax.annotation.Nonnull;

import org.json.JSONObject;

import com.project.domain.event.DomainEvent;
import com.project.domain.event.DomainEventMetadata;

public record IntermediateEventRepresentation(
    @Nonnull JSONObject eventPayload,
    @Nonnull DomainEventMetadata eventMetadata,
    int eventVersion,
    @Nonnull Class<? extends DomainEvent> eventType
)
{}
