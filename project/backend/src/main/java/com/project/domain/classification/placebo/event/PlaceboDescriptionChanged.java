package com.project.domain.classification.placebo.event;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.project.domain.classification.IdentifiedDomainEvent;

public final class PlaceboDescriptionChanged extends IdentifiedDomainEvent
{

    @Nullable
    private final String description;

    public PlaceboDescriptionChanged(@Nonnull String identity, @Nullable String description)
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
    static PlaceboDescriptionChanged fromJson(
        @JsonProperty("identity") String identity,
        @JsonProperty("description") String description)
    {
        return new PlaceboDescriptionChanged(identity, description);
    }
}
