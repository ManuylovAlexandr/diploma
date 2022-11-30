package com.project.domain.classification.placebo.event;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.domain.classification.IdentifiedDomainEvent;
import com.project.domain.classification.NewArtifactAddedEvent;

public final class NewPlaceboAdded extends IdentifiedDomainEvent implements NewArtifactAddedEvent
{

    @Nonnull
    private final String canonicalName;

    public NewPlaceboAdded(@Nonnull String identity, @Nonnull String canonicalName)
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
    static NewPlaceboAdded fromJson(
        @JsonProperty("identity") String surgeryId,
        @JsonProperty("canonicalName") String canonicalName
    )
    {
        return new NewPlaceboAdded(surgeryId, canonicalName);
    }
}
