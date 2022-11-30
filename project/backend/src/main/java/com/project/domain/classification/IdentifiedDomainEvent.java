package com.project.domain.classification;

import javax.annotation.Nonnull;

import com.project.domain.event.DomainEvent;

public abstract class IdentifiedDomainEvent extends DomainEvent
{

    private final String identity;

    protected IdentifiedDomainEvent(@Nonnull String identity)
    {
        this.identity = identity;
    }

    @Nonnull
    public String identity()
    {
        return this.identity;
    }

    @Nonnull
    @Override
    public String streamId()
    {
        return this.identity;
    }
}
