package com.project.domain.event;

import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface EventStore
{

   int MAX_BATCH_SIZE = 1000;

    @Nonnull
     List<StoredEvent> loadEventsFromStream(@Nonnull String streamId, @Nonnull Class<?> entityType);

     void addEventsToStream(
        @Nonnull String streamId,
        @Nonnull Class<?> entityType,
        @Nonnull List<? extends DomainEvent> events,
        @Nonnull EventMetadata eventMetadata
    );

    @Nonnull
     StoredEventsBatch loadEvents(
        @Nullable Long lastSeenEventId,
        int batchSize,
        @Nonnull Class<?> entityType,
        @Nonnull Set<Class<? extends DomainEvent>> eventTypes
    );

    @Nonnull
     StoredEventsBatch loadEvents(@Nullable Long lastSeenEventId, int batchSize);

    @Nonnull
     StoredEventsBatch loadEvents(@Nullable Long lastSeenEventId, int batchSize, @Nonnull Class<?> entityType);

     long loadLastEventIdFromStream(@Nonnull String streamId, @Nonnull Class<?> entityType);

     long loadLastEventIdFromType(@Nonnull Class<?> entityType);
}
