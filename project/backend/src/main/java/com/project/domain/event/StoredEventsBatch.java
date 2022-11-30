package com.project.domain.event;

import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;

import java.util.List;

import javax.annotation.Nonnull;

public class StoredEventsBatch
{

    @Nonnull
    private final List<StoredEvent> storedEvents;

    private final boolean hasNextBatch;

    public StoredEventsBatch(@Nonnull List<StoredEvent> storedEvents, boolean hasNextBatch)
    {
        this.storedEvents =storedEvents;
        this.hasNextBatch = hasNextBatch;
    }

    public StoredEventsBatch(boolean hasNextBatch)
    {
        this.storedEvents = emptyList();
        this.hasNextBatch = hasNextBatch;
    }

    @Nonnull
    public List<StoredEvent> storedEvents()
    {
        return unmodifiableList(this.storedEvents);
    }

    public boolean isEmpty()
    {
        return !this.isNotEmpty();
    }

    public boolean isNotEmpty()
    {
        return !this.storedEvents.isEmpty();
    }

    public long lastEventId()
    {
        if (isNotEmpty())
        {
            return this.storedEvents.get(this.storedEvents.size() - 1).id();
        } else
        {
            throw new IllegalStateException("Can not obtain last event is of empty batch.");
        }
    }

    public long firstEventId()
    {
        if (isNotEmpty())
        {
            return this.storedEvents.get(0).id();
        } else
        {
            throw new IllegalStateException("Can not obtain the first event is of empty batch.");
        }
    }

    public boolean hasNext()
    {
        return this.hasNextBatch;
    }
}
