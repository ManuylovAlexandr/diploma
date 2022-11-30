package com.project.domain.classification.radiotherapy.event;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.project.domain.classification.IdentifiedDomainEvent;

public final class RadiotherapyCanonicalNameChanged extends IdentifiedDomainEvent
{

    @Nonnull
    private final String canonicalName;

    public RadiotherapyCanonicalNameChanged(@Nonnull String identity, @Nonnull String canonicalName)
    {
        super(identity);
        this.canonicalName = canonicalName;
    }

    @Nonnull
    public String canonicalName()
    {
        return this.canonicalName;
    }

    @JsonCreator
    protected static RadiotherapyCanonicalNameChanged fromJson(
        @JsonProperty("identity") String surgeryId,
        @JsonProperty("canonicalName") String canonicalName
    )
    {
        return new RadiotherapyCanonicalNameChanged(surgeryId, canonicalName);
    }
}

