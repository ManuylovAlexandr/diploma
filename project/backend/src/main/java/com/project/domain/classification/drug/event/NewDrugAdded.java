package com.project.domain.classification.drug.event;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.project.domain.classification.drug.Drug;
import com.project.domain.classification.NewArtifactAddedEvent;

public final class NewDrugAdded extends DrugEvent implements NewArtifactAddedEvent
{

    @Nonnull
    private final String canonicalName;

    public NewDrugAdded(@Nonnull Drug.Id drugId, @Nonnull Drug.Name canonicalName)
    {
        super(drugId);
        this.canonicalName = canonicalName.asText();
    }

    @Nonnull
    public Drug.Name canonicalName()
    {
        return new Drug.Name(this.canonicalName);
    }

    @JsonCreator
    static NewDrugAdded fromJson(
        @JsonProperty("drugId") String drugId,
        @JsonProperty("canonicalName") String canonicalName
    )
    {
        return new NewDrugAdded(new Drug.Id(drugId), new Drug.Name(canonicalName));
    }
}
