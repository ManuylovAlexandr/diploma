package com.project.domain.classification.therapyclass.event;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.project.domain.classification.therapyclass.ArtifactClass;

public final class CanonicalNameChanged extends ArtifactClassEvent
{

    @Nonnull
    private final String canonicalName;

    public CanonicalNameChanged(@Nonnull ArtifactClass.Id identity, @Nonnull ArtifactClass.Name canonicalName)
    {
        super(identity);
        this.canonicalName = canonicalName.asText();
    }

    @Nonnull
    public final ArtifactClass.Name canonicalName()
    {
        return new ArtifactClass.Name(this.canonicalName);
    }

    @JsonCreator
    protected static CanonicalNameChanged fromJson(
        @JsonProperty("identity") String identity,
        @JsonProperty("canonicalName") String canonicalName)
    {
        return new CanonicalNameChanged(new ArtifactClass.Id(identity), new ArtifactClass.Name(canonicalName));
    }
}
