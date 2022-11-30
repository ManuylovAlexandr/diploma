package com.project.domain.classification.placebo.event;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.project.domain.classification.IdentifiedDomainEvent;

public final class PlaceboParentClassChanged extends IdentifiedDomainEvent
{

    @Nullable
    private final String parentClassId;

    public static PlaceboParentClassChanged removeFromClassification(@Nonnull String surgeryId)
    {
        return new PlaceboParentClassChanged(surgeryId, null);
    }

    public PlaceboParentClassChanged(@Nonnull String identity, @Nullable String parentClassId)
    {
        super(identity);
        this.parentClassId = parentClassId;
    }

    @Nonnull
    public Optional<String> parentClass()
    {
        return Optional.ofNullable(this.parentClassId);
    }

    @JsonCreator
    protected static PlaceboParentClassChanged fromJson(
        @JsonProperty("identity") String surgeryId,
        @JsonProperty("parentClassId") String parentClassId)
    {
        return new PlaceboParentClassChanged(surgeryId, parentClassId);
    }
}
