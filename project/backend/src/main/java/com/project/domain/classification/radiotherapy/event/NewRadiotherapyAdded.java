package com.project.domain.classification.radiotherapy.event;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.domain.classification.IdentifiedDomainEvent;
import com.project.domain.classification.NewArtifactAddedEvent;

public final class NewRadiotherapyAdded extends IdentifiedDomainEvent implements NewArtifactAddedEvent
{

    @Nonnull
    private final String canonicalName;

    public NewRadiotherapyAdded(@Nonnull String identity, @Nonnull String canonicalName)
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
    static NewRadiotherapyAdded fromJson(
        @JsonProperty("identity") String surgeryId,
        @JsonProperty("canonicalName") String canonicalName
    )
    {
        return new NewRadiotherapyAdded(surgeryId, canonicalName);
    }
}
