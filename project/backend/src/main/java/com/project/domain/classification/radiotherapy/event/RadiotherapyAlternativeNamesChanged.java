package com.project.domain.classification.radiotherapy.event;

import java.util.Set;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.project.domain.classification.IdentifiedDomainEvent;

public final class RadiotherapyAlternativeNamesChanged extends IdentifiedDomainEvent
{

    @Nonnull
    private final Set<String> alternativeNames;

    public RadiotherapyAlternativeNamesChanged(@Nonnull String identity, @Nonnull Set<String> alternativeNames)
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
    protected static RadiotherapyAlternativeNamesChanged fromJson(
        @JsonProperty("identity") String surgeryId,
        @JsonProperty("alternativeNames") Set<String> alternativeNames
    )
    {
        return new RadiotherapyAlternativeNamesChanged(surgeryId, alternativeNames);
    }
}
