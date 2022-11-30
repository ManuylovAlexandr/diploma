package com.project.domain.classification.therapyclass.event;

import static java.util.stream.Collectors.toSet;

import java.util.Set;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.project.domain.classification.therapyclass.ArtifactClass;

public final class AlternativeNamesChanged extends ArtifactClassEvent
{

    @Nonnull
    private final Set<String> alternativeNames;

    public AlternativeNamesChanged(
        @Nonnull ArtifactClass.Id identity,
        @Nonnull Set<ArtifactClass.Name> alternativeNames
    )
    {
        super(identity);
        this.alternativeNames = alternativeNames.stream()
            .map(ArtifactClass.Name::asText)
            .collect(toSet());
    }

    @Nonnull
    public Set<ArtifactClass.Name> alternativeNames()
    {
        return this.alternativeNames.stream().map(ArtifactClass.Name::new).collect(toSet());
    }

    @JsonCreator
    protected static AlternativeNamesChanged fromJson(
        @JsonProperty("identity") String identity,
        @JsonProperty("alternativeNames") Set<String> alternativeNames)
    {
        return new AlternativeNamesChanged(
            new ArtifactClass.Id(identity),
            alternativeNames.stream()
                .map(ArtifactClass.Name::new)
                .collect(toSet())
        );
    }
}
