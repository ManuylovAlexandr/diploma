package com.project.domain.classification.surgery.event;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.project.domain.classification.IdentifiedDomainEvent;

public final class SurgeryParentClassChanged extends IdentifiedDomainEvent
{

    @Nullable
    private final String parentClassId;

    public static SurgeryParentClassChanged removeFromClassification(@Nonnull String surgeryId)
    {
        return new SurgeryParentClassChanged(surgeryId, null);
    }

    public SurgeryParentClassChanged(@Nonnull String identity, @Nullable String parentClassId)
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
    protected static SurgeryParentClassChanged fromJson(
        @JsonProperty("identity") String surgeryId,
        @JsonProperty("parentClassId") String parentClassId)
    {
        return new SurgeryParentClassChanged(surgeryId, parentClassId);
    }
}
