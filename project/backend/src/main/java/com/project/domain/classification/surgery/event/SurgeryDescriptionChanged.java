package com.project.domain.classification.surgery.event;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.project.domain.classification.IdentifiedDomainEvent;

public final class SurgeryDescriptionChanged extends IdentifiedDomainEvent
{

    @Nullable
    private final String description;

    public SurgeryDescriptionChanged(@Nonnull String identity, @Nullable String description)
    {
        super(identity);
        this.description = description == null || description.isBlank() ? null : description;
    }

    @Nonnull
    public Optional<String> description()
    {
        return Optional.ofNullable(this.description);
    }

    @JsonCreator
    static SurgeryDescriptionChanged fromJson(
        @JsonProperty("identity") String identity,
        @JsonProperty("description") String description)
    {
        return new SurgeryDescriptionChanged(identity, description);
    }
}
