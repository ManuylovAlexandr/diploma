package com.project.domain.treatmentoption.event;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.project.domain.classification.IdentifiedDomainEvent;
import com.project.domain.treatmentoption.TreatmentOption;
import com.project.domain.treatmentoption.TreatmentOptionComponent;
import com.project.domain.treatmentoption.TreatmentOptionRelationToComponents;

public final class NewTreatmentOptionAdded extends IdentifiedDomainEvent
{

    @Nonnull
    private final String canonicalName;

    @Nonnull
    private final Set<TreatmentOptionComponent.Id> components;

    @Nonnull
    private final TreatmentOptionRelationToComponents relationToComponents;

    public NewTreatmentOptionAdded(
        @Nonnull TreatmentOption.Id identity,
        @Nonnull TreatmentOption.Name canonicalName,
        @Nonnull Set<TreatmentOptionComponent.Id> components,
        @Nonnull TreatmentOptionRelationToComponents relationToComponents
    )
    {
        super(identity.asText());
        this.canonicalName = canonicalName.asText();
        this.components = components;
        this.relationToComponents = relationToComponents;
    }

    @Nonnull
    public TreatmentOption.Id treatmentOptionId()
    {
        return TreatmentOption.Id.of(identity());
    }

    @Nonnull
    public TreatmentOption.Name canonicalName()
    {
        return TreatmentOption.Name.of(this.canonicalName);
    }

    @Nonnull
    public Set<TreatmentOptionComponent.Id> components()
    {
        return Collections.unmodifiableSet(this.components);
    }

    @Nonnull
    public TreatmentOptionRelationToComponents relationToComponents()
    {
        return this.relationToComponents;
    }

    @JsonCreator
    protected static NewTreatmentOptionAdded fromJson(
        @JsonProperty("identity") String identity,
        @JsonProperty("canonicalName") String canonicalName,
        @JsonProperty("components") List<TreatmentOptionComponent.Id> components,
        @JsonProperty("relationToComponents") TreatmentOptionRelationToComponents relationToComponents
    )
    {
        return new NewTreatmentOptionAdded(
            TreatmentOption.Id.of(identity),
            TreatmentOption.Name.of(canonicalName),
            new LinkedHashSet<>(components),
            relationToComponents
        );
    }
}
