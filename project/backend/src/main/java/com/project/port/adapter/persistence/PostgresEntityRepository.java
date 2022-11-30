package com.project.port.adapter.persistence;

import static com.project.common.CollectionUtils.map;
import static com.project.common.CollectionUtils.mapToList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import javax.annotation.Nonnull;

import org.springframework.stereotype.Repository;

import com.project.common.EventMetadataHolder;
import com.project.domain.event.DomainEvent;
import com.project.domain.event.EventStore;
import com.project.domain.event.StoredEventDeserializer;
import com.project.domain.event.StoredEventsBatch;
import com.project.domain.classification.AggregateRoot;

@Repository
public class PostgresEntityRepository
{

    @Nonnull
    private final EventStore eventStore;

    @Nonnull
    private final StoredEventDeserializer deserializer;

    private static final int INITIAL_CAPACITY = 2800;

    public PostgresEntityRepository(
        @Nonnull EventStore eventStore,
        @Nonnull StoredEventDeserializer deserializer)
    {
        this.eventStore = eventStore;
        this.deserializer = deserializer;
    }

    @Nonnull
    public <R> Collection<R> findAll(
        Class<?> entityType,
        Function<List<DomainEvent>, R> eventMapper)
    {
        var eventsPerStreamId = new HashMap<String, List<DomainEvent>>(INITIAL_CAPACITY);
        StoredEventsBatch storedEventsBatch;
        Long lastSeenEventId = null;
        do
        {
            storedEventsBatch = this.eventStore.loadEvents(
                lastSeenEventId,
                EventStore.MAX_BATCH_SIZE,
                entityType
            );
            storedEventsBatch.storedEvents().forEach(
                storedEvent -> eventsPerStreamId.computeIfAbsent(storedEvent.streamId(), id -> new ArrayList<>())
                    .add(this.deserializer.deserializeEvent(storedEvent, storedEvent.eventType()))
            );
            if (storedEventsBatch.isNotEmpty())
            {
                lastSeenEventId = storedEventsBatch.lastEventId();
            }
        } while (storedEventsBatch.hasNext());
        return mapToList(eventsPerStreamId.values(), eventMapper);
    }

    @Nonnull
    public <R> Optional<R> entityOfId(
        @Nonnull String entityId,
        @Nonnull Class<?> entityType,
        @Nonnull Function<List<DomainEvent>, R> eventMapper)
    {
        var storedEvents = this.eventStore.loadEventsFromStream(entityId, entityType);
        if (storedEvents.isEmpty())
        {
            return Optional.empty();
        } else
        {
            var events = map(storedEvents, this.deserializer::deserializeEvent);
            return Optional.of(eventMapper.apply(events));
        }
    }

    public <T extends DomainEvent, E extends AggregateRoot<T>> void save(
        @Nonnull E entity,
        Class<?> entityType,
        String id)
    {
        this.eventStore.addEventsToStream(
            id,
            entityType,
            entity.accumulatedEvents(),
            EventMetadataHolder.builder().build()
        );
    }
}
