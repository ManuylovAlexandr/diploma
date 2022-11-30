package com.project.domain.termquery;

import java.util.List;

import javax.annotation.Nonnull;

import com.project.domain.classification.AggregateRootId;
import com.project.domain.classification.CaseInsensitiveName;
import com.project.domain.event.DomainEvent;
import com.project.domain.termquery.events.NewTermQueryAdded;

public class TermQuery
{

    private final TermQueryState state;

    @Nonnull
    public static TermQuery fromEvents(@Nonnull List<DomainEvent> events)
    {
        return new TermQuery(events);
    }

    public TermQuery(@Nonnull NewTermQueryAdded event)
    {
        this.state = new TermQueryState(event);
    }

    public TermQuery(@Nonnull List<DomainEvent> events)
    {
        this.state = new TermQueryState(events);
    }

    public static final class Id extends AggregateRootId
    {

        private Id(@Nonnull String id)
        {
            super(id);
        }

        @Nonnull
        public static TermQuery.Id of(@Nonnull String identity)
        {
            return new TermQuery.Id(identity);
        }
    }

    public static final class Name extends CaseInsensitiveName
    {

        private Name(@Nonnull String name)
        {
            super(name);
        }

        @Nonnull
        public static TermQuery.Name of(@Nonnull String text)
        {
            return new TermQuery.Name(text);
        }
    }

    @Nonnull
    public TermQueryState state()
    {
        return this.state;
    }

    @Nonnull
    public Id id()
    {
        return this.state.id();
    }

    @Nonnull
    public Name canonicalName()
    {
        return this.state.canonicalName();
    }

    @Nonnull
    public String query()
    {
        return this.state.query();
    }
}
