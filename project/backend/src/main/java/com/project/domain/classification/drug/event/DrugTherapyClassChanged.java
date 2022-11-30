package com.project.domain.classification.drug.event;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.project.domain.classification.therapyclass.ArtifactClass;
import com.project.domain.classification.drug.Drug;

public final class DrugTherapyClassChanged extends DrugEvent
{

    @Nullable
    private final ArtifactClass.Id therapyClassId;

    public DrugTherapyClassChanged(@Nonnull Drug.Id drugId, @Nullable ArtifactClass.Id therapyClass)
    {
        super(drugId);
        this.therapyClassId = therapyClass;
    }

    @Nonnull
    public Optional<ArtifactClass.Id> therapyClass()
    {
        return Optional.ofNullable(this.therapyClassId);
    }

    @JsonCreator
    private static DrugTherapyClassChanged fromJson(
        @JsonProperty("drugId") String drugId,
        @JsonProperty("therapyClassId") String therapyClassId)
    {
        return new DrugTherapyClassChanged(
            new Drug.Id(drugId),
            Optional.ofNullable(therapyClassId).map(ArtifactClass.Id::new).orElse(null)
        );
    }

    @JsonGetter("therapyClassId")
    public String jsonTherapyClassId()
    {
        return Optional.ofNullable(this.therapyClassId)
            .map(ArtifactClass.Id::asText)
            .orElse(null);
    }
}
