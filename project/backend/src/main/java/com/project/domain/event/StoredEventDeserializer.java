package com.project.domain.event;

import static com.project.ProjectApplication.TransactionManagementConfiguration.TRANSACTION_INTERCEPTOR_ORDER;
import static java.util.Collections.reverse;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toCollection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import com.project.domain.event.deserializers.EventUpcaster;
import com.project.domain.event.deserializers.EventUpcasterChain;
import com.project.domain.event.deserializers.IntermediateEventRepresentation;
import com.project.application.ObjectMapperBuilder;


@Component
public class StoredEventDeserializer implements Ordered, InitializingBean
{

    private final List<EventUpcaster> eventUpcasters;

    private static final Map<VersionedEventType, EventUpcaster> CUSTOM_EVENT_DESERIALIZERS = new HashMap<>();

    private static final ObjectMapper OBJECT_MAPPER = ObjectMapperBuilder.builder()
        .withAllModules()
        .useFields()
        .build();

    public StoredEventDeserializer(List<EventUpcaster> eventUpcasters)
    {
        this.eventUpcasters = eventUpcasters;
    }

    @Nonnull
    public <T extends DomainEvent> T deserialize(
        @Nonnull String payload,
        @Nonnull Class<T> eventClazz,
        int eventVersion,
        @Nonnull DomainEventMetadata metadata
    )
    {
        T deserializedEvent;
        try
        {
            var versionedEventType = new VersionedEventType(eventVersion, eventClazz);
            var representation = new IntermediateEventRepresentation(
                new JSONObject(payload),
                metadata,
                eventVersion,
                eventClazz
            );
            if (CUSTOM_EVENT_DESERIALIZERS.containsKey(versionedEventType)
                && CUSTOM_EVENT_DESERIALIZERS.get(versionedEventType).canUpcast(representation))
            {
                var deserialized = CUSTOM_EVENT_DESERIALIZERS.get(versionedEventType)
                    .upcast(representation);
                payload = deserialized.eventPayload().toString();
            }
            deserializedEvent = OBJECT_MAPPER.readValue(payload, eventClazz);
            deserializedEvent.metadata(metadata);
            return deserializedEvent;
        }
        catch (IOException e)
        {
            throw new EventDeserializationException("Failed to deserialize event", e);
        }
    }

    @Nonnull
    public DomainEvent deserializeEvent(@Nonnull StoredEvent event)
    {
        var metadata = new DomainEventMetadata(event.occurredOn(), event.id());
        return this.deserialize(event.eventPayload(), event.eventType(), 1, metadata);
    }

    @Nonnull
    public <T extends DomainEvent> T deserializeEvent(@Nonnull StoredEvent event, @Nonnull Class<T> eventType)
    {

        return eventType.cast(deserializeEvent(event));
    }

    @Override
    public int getOrder()
    {
        return TRANSACTION_INTERCEPTOR_ORDER - 1;
    }

    @Override
    public void afterPropertiesSet()
    {
        initUpcasterChains();
    }

    private void initUpcasterChains()
    {
        var upcastersGrouped =
            this.eventUpcasters.stream()
                .collect(groupingBy(
                    u -> u.versionedEventType().eventType(),
                    collectingAndThen(
                        toCollection(() -> new TreeSet<>(comparing(
                            (EventUpcaster u) -> u.versionedEventType().eventVersion())
                            .reversed())),
                        ArrayList::new
                    )
                ));

        for (var entry : upcastersGrouped.entrySet())
        {
            List<EventUpcaster> singleEventUpcasters = new ArrayList<>();
            for (var upcaster : entry.getValue())
            {
                singleEventUpcasters.add(upcaster);
                var reversedEventUpcasters = new ArrayList<>(singleEventUpcasters);
                reverse(reversedEventUpcasters);
                CUSTOM_EVENT_DESERIALIZERS.put(
                    new VersionedEventType(upcaster.versionedEventType().eventVersion(), entry.getKey()),
                    new EventUpcasterChain(reversedEventUpcasters)
                );
            }
        }
    }
}
