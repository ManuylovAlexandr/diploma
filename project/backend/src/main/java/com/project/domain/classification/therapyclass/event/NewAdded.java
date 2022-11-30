package com.project.domain.classification.therapyclass.event;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.domain.classification.therapyclass.ArtifactClass;

public final class NewAdded extends ArtifactClassEvent
{

    @Nonnull
    private final String artifactClassType;

    @Nonnull
    private final String canonicalName;

    @Nullable
    private final ArtifactClass.Id superClassId;

    public NewAdded(
        @Nonnull ArtifactClass.Id identity,
        @Nonnull String artifactClassType,
        @Nonnull ArtifactClass.Name canonicalName,
        @Nullable ArtifactClass.Id superClass
    )
    {
        super(identity);
        this.artifactClassType = artifactClassType;
        this.canonicalName = canonicalName.asText();
        this.superClassId = superClass;
    }

    @Nonnull
    public String artifactClassType()
    {
        return this.artifactClassType;
    }

    @Nonnull
    public ArtifactClass.Name canonicalName()
    {
        return new ArtifactClass.Name(this.canonicalName);
    }

    @Nonnull
    public Optional<ArtifactClass.Id> superClass()
    {
        return Optional.ofNullable(this.superClassId);
    }

    @JsonCreator
    static NewAdded fromJson(
        @JsonProperty("identity") String identity,
        @JsonProperty("artifactClassType") String artifactClassType,
        @JsonProperty("canonicalName") String canonicalName,
        @JsonProperty("superClassId") String superClassId
    )
    {
        var ids = Optional.ofNullable(superClassId)
            .map(ArtifactClass.Id::new)
            .orElse(null);

        return new NewAdded(
            new ArtifactClass.Id(identity),
            artifactClassType,
            new ArtifactClass.Name(canonicalName),
            ids
        );
    }

    @JsonGetter("superClassId")
    public String jsonSuperClassId()
    {
        return Optional.ofNullable(this.superClassId)
            .map(ArtifactClass.Id::asText)
            .orElse(null);
    }
}
