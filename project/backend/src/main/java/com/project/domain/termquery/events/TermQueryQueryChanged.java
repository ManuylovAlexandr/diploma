package com.project.domain.termquery.events;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.project.domain.termquery.TermQuery;

public final class TermQueryQueryChanged extends TermQueryEvent
{

    @Nonnull
    private final String query;

    public TermQueryQueryChanged(@Nonnull TermQuery.Id identity, @Nonnull String query)
    {
        super(identity);
        this.query = query;
    }

    public String query()
    {
        return this.query;
    }

    @JsonCreator
    static TermQueryQueryChanged fromJson(
        @JsonProperty("identity") String identity,
        @JsonProperty("query") String query
    )
    {
        return new TermQueryQueryChanged(
            TermQuery.Id.of(identity),
            query
        );
    }
}
