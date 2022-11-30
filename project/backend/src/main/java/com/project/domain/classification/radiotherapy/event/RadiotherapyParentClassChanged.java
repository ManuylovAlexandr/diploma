package com.project.domain.classification.radiotherapy.event;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.project.domain.classification.IdentifiedDomainEvent;

public final class RadiotherapyParentClassChanged extends IdentifiedDomainEvent
{

    @Nullable
    private final String parentClassId;

    public static RadiotherapyParentClassChanged removeFromClassification(@Nonnull String radiotherapyId)
    {
        return new RadiotherapyParentClassChanged(radiotherapyId, null);
    }

    public RadiotherapyParentClassChanged(@Nonnull String identity, @Nullable String parentClassId)
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
    protected static RadiotherapyParentClassChanged fromJson(
        @JsonProperty("identity") String surgeryId,
        @JsonProperty("parentClassId") String parentClassId
    )
    {
        return new RadiotherapyParentClassChanged(surgeryId, parentClassId);
    }
}
