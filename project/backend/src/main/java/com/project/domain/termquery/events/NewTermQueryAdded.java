package com.project.domain.termquery.events;

import static java.util.stream.Collectors.toSet;

import java.util.Set;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.project.domain.termquery.TermQuery;

public final class NewTermQueryAdded extends TermQueryEvent
{

    @Nonnull
    private final TermQuery.Name canonicalName;

    @Nonnull
    private final String query;

    @Nonnull
    private final String description;

    @Nonnull
    private final Set<TermQuery.Name> alternativeNames;

    public NewTermQueryAdded(
        @Nonnull TermQuery.Id identity,
        @Nonnull TermQuery.Name canonicalName,
        @Nonnull String query,
        @Nonnull String description,
        @Nonnull Set<TermQuery.Name> alternativeNames)
    {
        super(identity);
        this.canonicalName = canonicalName;
        this.query = query;
        this.description = description;
        this.alternativeNames = alternativeNames;
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

    @Nonnull
    public String description()
    {
        return this.description;
    }

    @Nonnull
    public Set<TermQuery.Name> alternativeNames()
    {
        return this.alternativeNames;
    }

    @JsonCreator
    static NewTermQueryAdded fromJson(
        @JsonProperty("identity") String id,
        @JsonProperty("canonicalName") String canonicalName,
        @JsonProperty("query") String query,
        @JsonProperty("description") String description,
        @JsonProperty("alternativeNames") Set<String> alternativeNames
    )
    {
        return new NewTermQueryAdded(
            TermQuery.Id.of(id),
            TermQuery.Name.of(canonicalName),
            query,
            description,
            alternativeNames.stream().map(TermQuery.Name::of).collect(toSet())
        );
    }

    @JsonGetter("canonicalName")
    public String jsonCanonicalName()
    {
        return this.canonicalName.asText();
    }

    @JsonGetter("alternativeNames")
    public Set<String> jsonAlternativeNames()
    {
        return this.alternativeNames.stream().map(TermQuery.Name::asText).collect(toSet());
    }
}
