package com.project.domain.termquery;

import java.util.List;
import java.util.function.BiConsumer;

import javax.annotation.Nonnull;

import com.project.common.CompoundTypeSafeBiConsumer;
import com.project.domain.classification.AggregateRoot;
import com.project.domain.event.DomainEvent;
import com.project.domain.termquery.events.TermQueryCanonicalNameChanged;
import com.project.domain.termquery.events.TermQueryQueryChanged;
import com.project.domain.termquery.events.NewTermQueryAdded;

public class TermQueryState extends AggregateRoot<DomainEvent>
{

    private static final BiConsumer<DomainEvent, TermQueryState> EVENTS_APPLIER =
        CompoundTypeSafeBiConsumer.<DomainEvent, TermQueryState>builder()
            .withConsumer(
                NewTermQueryAdded.class,
                (event, state) -> state.applyNewAdded(event)
            )
            .withConsumer(
                TermQueryCanonicalNameChanged.class,
                (event, state) -> state.setCanonicalName(event.canonicalName())
            )
            .withConsumer(
                TermQueryQueryChanged.class,
                (event, state) -> state.setQuery(event.query())
            )
            .build();

    private TermQuery.Id id;

    private TermQuery.Name canonicalName;

    private String query;

    public TermQueryState(@Nonnull NewTermQueryAdded event)
    {
        publish(event);
    }

    public TermQueryState(@Nonnull List<DomainEvent> events)
    {

        events.forEach(this::applyEvent);
    }

    @Override
    protected void applyEvent(DomainEvent event)
    {

        EVENTS_APPLIER.accept(event, this);
    }

    public void applyNewAdded(NewTermQueryAdded event)
    {
        this.id = event.id();
        this.canonicalName = event.canonicalName();
        this.query = event.query();
    }

    @Nonnull
    public TermQuery.Id id()
    {
        return this.id;
    }

    @Nonnull
    public TermQuery.Name canonicalName()
    {
        return this.canonicalName;
    }

    @Nonnull
    public String query()
    {
        return this.query;
    }

    public void setId(@Nonnull TermQuery.Id id)
    {
        this.id = id;
    }

    public void setCanonicalName(@Nonnull TermQuery.Name canonicalName)
    {
        this.canonicalName = canonicalName;
    }

    public void setQuery(@Nonnull String query)
    {
        this.query = query;
    }
}
