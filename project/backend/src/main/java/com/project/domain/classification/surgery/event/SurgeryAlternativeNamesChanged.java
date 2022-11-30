package com.project.domain.classification.surgery.event;

import java.util.Set;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.project.domain.classification.IdentifiedDomainEvent;

public final class SurgeryAlternativeNamesChanged extends IdentifiedDomainEvent
{

    @Nonnull
    private final Set<String> alternativeNames;

    public SurgeryAlternativeNamesChanged(@Nonnull String identity, @Nonnull Set<String> alternativeNames)
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
    protected static SurgeryAlternativeNamesChanged fromJson(
        @JsonProperty("identity") String surgeryId,
        @JsonProperty("alternativeNames") Set<String> alternativeNames)
    {
        return new SurgeryAlternativeNamesChanged(surgeryId, alternativeNames);
    }
}
