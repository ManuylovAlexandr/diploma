package com.project.port.adapter.persistence;

import static java.util.stream.Collectors.toSet;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.sql.DataSource;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.project.common.ObjectSerializer;
import com.project.domain.event.DomainEvent;
import com.project.domain.event.DomainEventSerializer;
import com.project.domain.event.EventMetadata;
import com.project.domain.event.EventStore;
import com.project.domain.event.EventTypeRegistry;
import com.project.domain.event.StoredEvent;
import com.project.domain.event.StoredEventsBatch;

@Repository
public class PostgresEventStore implements EventStore
{

    public static final String STREAM_ID = "streamId";

    public static final String EVENT_TYPES = "eventTypes";

    @Nonnull
    private final NamedParameterJdbcOperations jdbcOperations;

    @Nonnull
    private final EventStoreClock clock;

    @Nonnull
    private final DomainEventSerializer serializer;

    public PostgresEventStore(
        @Nonnull DataSource dataSource,
        @Nonnull EventStoreClock clock,
        @Nonnull DomainEventSerializer serializer)
    {
        this.jdbcOperations = new NamedParameterJdbcTemplate(dataSource);
        this.clock = clock;
        this.serializer = serializer;
    }

    @Nonnull
    @Override
    public List<StoredEvent> loadEventsFromStream(
        @Nonnull String streamId,
        @Nonnull Class<?> entityType)
    {
        var sqlQuery = """
            select id, stream_id, occurred_on, event_type, event_payload::text, event_metadata::text
              from stored_events
             where stream_id = :streamId and event_type in (:eventTypes)
             order by id
            """;

        var eventTypes = eventTypeNamesOfType(entityType);
        var queryParams = Map.of(
            STREAM_ID, streamId,
            EVENT_TYPES, eventTypes
        );
        return this.jdbcOperations.query(
            sqlQuery,
            queryParams,
            (row, index) -> asStoredEvent(row)
        );
    }

    @Override
    public void addEventsToStream(
        @Nonnull String streamId,
        @Nonnull Class<?> entityType,
        @Nonnull List<? extends DomainEvent> events,
        @Nonnull EventMetadata eventMetadata
    )
    {
        if (!events.isEmpty())
        {
            var sqlQuery = """
                insert into stored_events(stream_id, occurred_on,
                                          event_type, event_payload, event_metadata)
                values(:streamId, :occurredOn, :eventType,
                       :eventPayload::text::jsonb, :eventMetadata::text::jsonb)
                """;

            var metadataJson = ObjectSerializer.instance().serialize(eventMetadata);
            var queryParams = events.stream()
                .map(domainEvent -> Map.of(
                    STREAM_ID, streamId,
                    "occurredOn", this.clock.now(),
                    "eventType", EventTypeRegistry.instance().typeNameOf(entityType, domainEvent.getClass()),
                    "eventPayload", this.serializer.serialize(domainEvent),
                    "eventMetadata", metadataJson
                ))
                .map(MapSqlParameterSource::new)
                .toArray(SqlParameterSource[]::new);
            this.jdbcOperations.batchUpdate(sqlQuery, queryParams);
        }
    }

    @Nonnull
    @Override
    public StoredEventsBatch loadEvents(
        @Nullable Long lastSeenEventId,
        int batchSize,
        @Nonnull Class<?> entityType,
        @Nonnull Set<Class<? extends DomainEvent>> eventTypes)
    {

        var sqlQuery = """
            select id, stream_id, occurred_on, event_type, event_payload::text, event_metadata::text
              from stored_events
             where id > :lastSeenEventId and event_type in(:eventTypes)
             order by id
             limit :batchSize
            """;

        var eventTypeNames = eventTypes.stream()
            .map(unspecificEventType -> eventTypeNameOf(entityType, unspecificEventType))
            .collect(toSet());

        var queryParams = Map.of(
            "lastSeenEventId", Optional.ofNullable(lastSeenEventId).orElse(0L),
            EVENT_TYPES, eventTypeNames,
            "batchSize", Integer.min(batchSize, MAX_BATCH_SIZE) + 1
        );
        var loadedEvents = this.jdbcOperations.query(
            sqlQuery,
            queryParams,
            (row, index) -> asStoredEvent(row)
        );
        return loadedEvents.size() <= batchSize
            ? new StoredEventsBatch(loadedEvents, false)
            : new StoredEventsBatch(loadedEvents.subList(0, batchSize), true);
    }

    @Nonnull
    @Override
    public StoredEventsBatch loadEvents(@Nullable Long lastSeenEventId, int batchSize)
    {

        var sqlQuery = """
            select id, stream_id, occurred_on, event_type, event_payload::text, event_metadata::text
              from stored_events
             where id > :lastSeenEventId
             order by id
             limit :batchSize
            """;

        var queryParams = Map.of(
            "lastSeenEventId", Optional.ofNullable(lastSeenEventId).orElse(0L),
            "batchSize", Integer.min(batchSize, MAX_BATCH_SIZE) + 1
        );
        var loadedEvents = this.jdbcOperations.query(
            sqlQuery,
            queryParams,
            (row, index) -> asStoredEvent(row)
        );
        return loadedEvents.size() <= batchSize
            ? new StoredEventsBatch(loadedEvents, false)
            : new StoredEventsBatch(loadedEvents.subList(0, batchSize), true);
    }

    @Nonnull
    @Override
    public StoredEventsBatch loadEvents(
        @Nullable Long lastSeenEventId, int batchSize, @Nonnull Class<?> entityType)
    {
        var entityTypeEvents = eventTypeOfType(entityType);
        return this.loadEvents(lastSeenEventId, batchSize, entityType, entityTypeEvents);
    }

    @Override
    public long loadLastEventIdFromStream(
        @Nonnull String streamId,
        @Nonnull Class<?> entityType)
    {
        var sqlQuery = """
                select max(e.id)
                  from stored_events e
                 where e.event_type in (:eventTypes)
                   and e.stream_id = :streamId
            """;

        var eventTypes = eventTypeNamesOfType(entityType);
        var queryParams = Map.of(
            STREAM_ID, streamId,
            EVENT_TYPES, eventTypes
        );

        Long lastEventId = this.jdbcOperations.queryForObject(sqlQuery, queryParams, Long.class);
        return (lastEventId != null) ? lastEventId : 0;
    }

    @Override
    public long loadLastEventIdFromType(@Nonnull Class<?> entityType)
    {
        var sqlQuery = """
                select max(e.id)
                 from stored_events e
                where e.event_type in (:eventTypes)
            """;

        var eventTypes = eventTypeNamesOfType(entityType);
        var queryParams = Map.of(
            EVENT_TYPES, eventTypes
        );

        Long lastEventId = this.jdbcOperations.queryForObject(sqlQuery, queryParams, Long.class);
        return (lastEventId != null) ? lastEventId : 0;
    }

    private static StoredEvent asStoredEvent(ResultSet row)
        throws SQLException
    {
        return StoredEvent.builder()
            .withId(row.getLong("id"))
            .withStreamId(row.getString("stream_id"))
            .occurredOn(row.getObject("occurred_on", OffsetDateTime.class))
            .withEventTypeName(row.getString("event_type"))
            .withEventPayload(row.getString("event_payload"))
            .withEventMetadataPayload(row.getString("event_metadata"))
            .build();
    }

    @Nonnull
    private static Set<String> eventTypeNamesOfType(Class<?> entityType)
    {
        return EventTypeRegistry.instance().eventTypeNamesOf(entityType);
    }

    @Nonnull
    private static Set<Class<? extends DomainEvent>> eventTypeOfType(Class<?> entityType)
    {
        return EventTypeRegistry.instance().eventTypesOf(entityType);
    }

    @Nonnull
    private static String eventTypeNameOf(Class<?> entityType, Class<? extends DomainEvent> eventType)
    {
        return EventTypeRegistry.instance().typeNameOf(entityType, eventType);
    }
}
