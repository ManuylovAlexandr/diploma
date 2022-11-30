package com.project.domain.termquery.events;


import javax.annotation.Nonnull;

import com.project.domain.event.DomainEvent;
import com.project.domain.termquery.TermQuery;

public abstract class TermQueryEvent extends DomainEvent
{

    @Nonnull
    private final String identity;

    protected TermQueryEvent(TermQuery.Id identity)
    {
        this.identity = identity.asText();
    }

    @Nonnull
    public TermQuery.Id id()
    {
        return TermQuery.Id.of(this.identity);
    }

    @Nonnull
    @Override
    public String streamId()
    {
        return this.identity;
    }
}
