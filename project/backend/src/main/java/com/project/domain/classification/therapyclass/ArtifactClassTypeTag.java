package com.project.domain.classification.therapyclass;

import java.util.Arrays;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public enum ArtifactClassTypeTag
{
    THERAPY_CLASS("therapy_tree", "TherapyClass"),
    SURGERY_CLASS("surgery_tree", "SurgeryClass"),
    RADIOTHERAPY_CLASS("radiotherapy_tree", "RadiotherapyClass"),
    PLACEBO_CLASS("placebo_tree", "PlaceboClass");

    private final String typeTag;

    private final ArtifactClass.Name typeTagClassName;

    private final ArtifactClass.Id typeTagClassId;

    private final String artifactClassType;

    ArtifactClassTypeTag(@Nonnull String typeTagClassId, @Nonnull String artifactClassType)
    {
        this.typeTagClassId = new ArtifactClass.Id(typeTagClassId);
        this.typeTagClassName = new ArtifactClass.Name(typeTagClassId);
        this.typeTag = typeTagClassId;
        this.artifactClassType = artifactClassType;
    }

    @Nonnull
    public ArtifactClass.Name typeTagClassName()
    {
        return this.typeTagClassName;
    }

    @Nonnull
    public ArtifactClass.Id typeTagClassId()
    {
        return this.typeTagClassId;
    }

    @Nonnull
    public String typeTag()
    {
        return this.typeTag;
    }

    @Nonnull
    public String artifactClassType()
    {
        return this.artifactClassType;
    }

    @Nullable
    public static ArtifactClassTypeTag classTypeTagOf(@Nonnull String classTypeTag)
    {
        return Arrays.stream(ArtifactClassTypeTag.values())
            .filter(knownTypeTag -> knownTypeTag.typeTag.equals(classTypeTag))
            .findFirst()
            .orElse(null);
    }

    @Nonnull
    public static ArtifactClassTypeTag classTypeTagOfArtifactClass(@Nonnull String artifactClassType)
    {
        return Arrays.stream(ArtifactClassTypeTag.values())
            .filter(knownTypeTag -> knownTypeTag.artifactClassType.equals(artifactClassType))
            .findFirst()
            .orElseThrow();
    }
}
