package com.project.domain.event;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class DomainEvent
{

    @Nonnull
    @JsonIgnore
    private DomainEventMetadata metadata;

    protected DomainEvent()
    {
        this.metadata = new DomainEventMetadata(null, null);
    }

    @Nonnull
    public abstract String streamId();

    public DomainEventMetadata metadata()
    {
        return this.metadata;
    }

    void metadata(DomainEventMetadata metadata)
    {
        this.metadata = metadata;
    }
}
