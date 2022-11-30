package com.project.domain.classification.therapyclass.event;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.project.domain.classification.therapyclass.ArtifactClass;

public final class SuperClassChanged extends ArtifactClassEvent
{

    @Nonnull
    private final ArtifactClass.Id superClass;

    public SuperClassChanged(@Nonnull ArtifactClass.Id identity, @Nonnull ArtifactClass.Id superClass)
    {
        super(identity);
        this.superClass = superClass;
    }

    @Nonnull
    public ArtifactClass.Id superClass()
    {
        return this.superClass;
    }

    @JsonCreator
    protected static SuperClassChanged fromJson(
        @JsonProperty("identity") String identity,
        @JsonProperty("superClass") String superClass)
    {
        return new SuperClassChanged(
            new ArtifactClass.Id(identity),
            new ArtifactClass.Id(superClass)
        );
    }

    @JsonGetter("superClass")
    public String jsonSuperClassId()
    {
        return this.superClass.asText();
    }
}
