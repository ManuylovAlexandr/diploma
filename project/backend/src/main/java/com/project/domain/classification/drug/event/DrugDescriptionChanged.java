package com.project.domain.classification.drug.event;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.project.domain.classification.drug.Drug;

public final class DrugDescriptionChanged extends DrugEvent
{

    @Nullable
    private final String description;

    public DrugDescriptionChanged(@Nonnull Drug.Id drugId, @Nullable String description)
    {
        super(drugId);
        this.description = description == null || description.isBlank() ? null : description;
    }

    @Nonnull
    public Optional<String> description()
    {
        return Optional.ofNullable(this.description);
    }

    @JsonCreator
    private static DrugDescriptionChanged fromJson(
        @JsonProperty("drugId") String drugId,
        @JsonProperty("description") String description)
    {
        return new DrugDescriptionChanged(
            new Drug.Id(drugId),
            description
        );
    }
}
