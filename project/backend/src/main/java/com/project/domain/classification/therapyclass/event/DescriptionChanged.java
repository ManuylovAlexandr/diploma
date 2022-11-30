package com.project.domain.classification.therapyclass.event;

import static java.util.function.Predicate.not;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.project.domain.classification.therapyclass.ArtifactClass;

public final class DescriptionChanged extends ArtifactClassEvent
{

    @Nullable
    private final String description;

    public DescriptionChanged(
        @Nonnull ArtifactClass.Id identity, @Nullable String description
    )
    {
        super(identity);
        this.description = description == null || description.isBlank() ? null : description;
    }

    @Nonnull
    public Optional<String> description()
    {
        return Optional.ofNullable(this.description).filter(not(String::isBlank));
    }

    @JsonCreator
    static DescriptionChanged fromJson(
        @JsonProperty("identity") String identity,
        @JsonProperty("description") String description)
    {
        return new DescriptionChanged(
            new ArtifactClass.Id(identity),
            description
        );
    }
}
