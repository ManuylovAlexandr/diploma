package com.project.domain.classification.placebo.event;

import java.util.Set;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.project.domain.classification.IdentifiedDomainEvent;

public final class PlaceboAlternativeNamesChanged extends IdentifiedDomainEvent
{

    @Nonnull
    private final Set<String> alternativeNames;

    public PlaceboAlternativeNamesChanged(@Nonnull String identity, @Nonnull Set<String> alternativeNames)
    {
        super(identity);
        this.alternativeNames = alternativeNames;
    }

    @Nonnull
    public Set<String> alternativeNames()
    {
        return this.alternativeNames;
    }

    @JsonCreator
    protected static PlaceboAlternativeNamesChanged fromJson(
        @JsonProperty("identity") String surgeryId,
        @JsonProperty("alternativeNames") Set<String> alternativeNames)
    {
        return new PlaceboAlternativeNamesChanged(surgeryId, alternativeNames);
    }
}
