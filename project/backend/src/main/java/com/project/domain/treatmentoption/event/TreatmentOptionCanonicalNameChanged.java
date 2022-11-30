package com.project.domain.treatmentoption.event;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.project.domain.classification.IdentifiedDomainEvent;
import com.project.domain.treatmentoption.TreatmentOption;

public final class TreatmentOptionCanonicalNameChanged extends IdentifiedDomainEvent
{

    @Nonnull
    private final String canonicalName;

    public TreatmentOptionCanonicalNameChanged(
        @Nonnull TreatmentOption.Id identity,
        @Nonnull TreatmentOption.Name canonicalName)
    {
        super(identity.asText());
        this.canonicalName = canonicalName.asText();
    }

    @Nonnull
    public TreatmentOption.Name canonicalName()
    {
        return TreatmentOption.Name.of(this.canonicalName);
    }

    @JsonCreator
    protected static TreatmentOptionCanonicalNameChanged fromJson(
        @JsonProperty("identity") String identity,
        @JsonProperty("canonicalName") String canonicalName)
    {
        return new TreatmentOptionCanonicalNameChanged(
            TreatmentOption.Id.of(identity),
            TreatmentOption.Name.of(canonicalName)
        );
    }
}
