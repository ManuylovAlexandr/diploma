package com.project.domain.treatmentoption.event;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.project.domain.classification.IdentifiedDomainEvent;
import com.project.domain.treatmentoption.TreatmentOption;

public final class TreatmentOptionDescriptionChanged extends IdentifiedDomainEvent
{

    @Nullable
    private final String description;

    public TreatmentOptionDescriptionChanged(
        @Nonnull TreatmentOption.Id identity,
        @Nullable String description)
    {
        super(identity.asText());
        this.description = description;
    }

    @Nonnull
    public Optional<String> description()
    {
        return Optional.ofNullable(this.description);
    }

    @JsonCreator
    private static TreatmentOptionDescriptionChanged fromJson(
        @JsonProperty("identity") String identity,
        @JsonProperty("description") String description)
    {
        return new TreatmentOptionDescriptionChanged(TreatmentOption.Id.of(identity), description);
    }
}
