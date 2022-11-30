package com.project.domain.classification.drug.event;


import javax.annotation.Nonnull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.project.domain.classification.drug.Drug;

public final class DrugCanonicalNameChanged extends DrugEvent
{

    @Nonnull
    private final String canonicalName;

    public DrugCanonicalNameChanged(@Nonnull Drug.Id drugId, @Nonnull Drug.Name canonicalName)
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
    protected static DrugCanonicalNameChanged fromJson(
        @JsonProperty("drugId") String drugId,
        @JsonProperty("canonicalName") String canonicalName)
    {
        return new DrugCanonicalNameChanged(
            new Drug.Id(drugId),
            new Drug.Name(canonicalName)
        );
    }
}
