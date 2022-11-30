package com.project.port.adapter.common.projections;


import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import javax.annotation.Nonnull;


import com.project.domain.event.DomainEvent;
import com.project.domain.event.StoredEvent;
import com.project.domain.event.StoredEventDeserializer;

public abstract class ProjectionMaintainingStoredEventHandler
{

    protected final StoredEventDeserializer deserializer;

    @Nonnull
    private final Map<Class<? extends DomainEvent>, StoredEventConsumer> eventConsumers = new HashMap<>();

    protected ProjectionMaintainingStoredEventHandler(StoredEventDeserializer deserializer)
    {
        this.deserializer = deserializer;
    }

    public final void handleEvent(@Nonnull StoredEvent event)
    {
        var consumer = this.eventConsumers.get(event.eventType());
        if (consumer != null)
        {
            consumer.accept(event);
        }
    }

    public abstract void reset();

    protected final <T extends DomainEvent> void registerHandler(
        @Nonnull Class<T> anEventType,
        @Nonnull StoredEventConsumer eventHandler
    )
    {

        checkNoHandlersRegisteredYet(Set.of(anEventType));
        this.eventConsumers.put(anEventType, eventHandler);
    }

    private void checkNoHandlersRegisteredYet(Set<Class<? extends DomainEvent>> eventTypes)
    {
        this.eventConsumers.keySet().stream()
            .filter(eventTypes::contains)
            .findAny()
            .ifPresent((Class<? extends DomainEvent> alreadyHandledType) -> {
                throw new IllegalStateException(
                    "Handler for type " + alreadyHandledType + " is already registered."
                );
            });
    }

    public interface StoredEventConsumer extends Consumer<StoredEvent>
    {
        // nop
    }
}
