package com.project.domain.classification;

import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import com.project.domain.event.DomainEvent;

public abstract class AggregateRoot<E extends DomainEvent>
{

    @Nonnull
    private final List<E> changesToState = new ArrayList<>();

    @Nonnull
    private EntityMetadata metadata;

    protected AggregateRoot()
    {
        this.metadata = new EntityMetadata(null, null, null);
    }

    @Nonnull
    public List<E> accumulatedEvents()
    {
        return unmodifiableList(this.changesToState);
    }

    public void clearAccumulatedEvents()
    {
        this.changesToState.clear();
    }

    protected abstract void applyEvent(E event);

    protected final void applyEventWithMeta(E event)
    {
        applyEvent(event);
        withMeta(
            new EntityMetadata(
                this.metadata.createdAt() != null ? this.metadata.createdAt() : event.metadata().occurredOn(),
                event.metadata().occurredOn(),
                event.metadata().revision()
            )
        );
    }

    protected final void withMeta(EntityMetadata metadata)
    {
        this.metadata = metadata;
    }

    public final void publish(E event)
    {
        this.applyEventWithMeta(event);
        this.changesToState.add(event);
    }

    public EntityMetadata metadata()
    {
        return this.metadata;
    }
}
