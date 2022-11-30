package com.project.domain.treatmentoption.event;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.project.domain.classification.IdentifiedDomainEvent;
import com.project.domain.treatmentoption.TreatmentOption;
import com.project.domain.treatmentoption.TreatmentOptionRelationToComponents;

public final class TreatmentOptionRelationToComponentsChanged extends IdentifiedDomainEvent
{

    @Nonnull
    private final TreatmentOptionRelationToComponents relationToComponents;

    public TreatmentOptionRelationToComponentsChanged(
        @Nonnull TreatmentOption.Id identity,
        @Nonnull TreatmentOptionRelationToComponents relationToComponents)
    {
        super(identity.asText());
        this.relationToComponents = relationToComponents;
    }

    @Nonnull
    public TreatmentOptionRelationToComponents relationToComponents()
    {
        return this.relationToComponents;
    }

    @JsonCreator
    protected static TreatmentOptionRelationToComponentsChanged fromJson(
        @JsonProperty("identity") String identity,
        @JsonProperty("relationToComponents") TreatmentOptionRelationToComponents relationToComponents)
    {
        return new TreatmentOptionRelationToComponentsChanged(TreatmentOption.Id.of(identity), relationToComponents);
    }
}
