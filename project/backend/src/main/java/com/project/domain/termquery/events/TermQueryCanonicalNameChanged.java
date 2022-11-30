package com.project.domain.termquery.events;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.project.domain.termquery.TermQuery;

public final class TermQueryCanonicalNameChanged extends TermQueryEvent
{

    @Nonnull
    private final TermQuery.Name canonicalName;

    public TermQueryCanonicalNameChanged(
        @Nonnull TermQuery.Id identity,
        @Nonnull TermQuery.Name canonicalName
    )
    {
        super(identity);
        this.canonicalName = canonicalName;
    }

    @Nonnull
    public TermQuery.Name canonicalName()
    {
        return this.canonicalName;
    }

    @JsonCreator
    static TermQueryCanonicalNameChanged fromJson(
        @JsonProperty("identity") String identity,
        @JsonProperty("canonicalName") String canonicalName
    )
    {
        return new TermQueryCanonicalNameChanged(
            TermQuery.Id.of(identity),
            TermQuery.Name.of(canonicalName)
        );
    }

    @JsonGetter("canonicalName")
    public String jsonCanonicalName()
    {
        return this.canonicalName.asText();
    }
}
