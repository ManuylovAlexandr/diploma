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

public final class TreatmentOptionComponentsChanged extends IdentifiedDomainEvent
{

    @Nonnull
    private final Set<TreatmentOptionComponent.Id> components;

    public TreatmentOptionComponentsChanged(
        @Nonnull TreatmentOption.Id identity,
        @Nonnull Set<TreatmentOptionComponent.Id> components
    )
    {
        super(identity.asText());
        this.components = components;
    }

    @Nonnull
    public Set<TreatmentOptionComponent.Id> components()
    {
        return Collections.unmodifiableSet(this.components);
    }

    @JsonCreator
    private static TreatmentOptionComponentsChanged fromJson(
        @JsonProperty("identity") String identity,
        @JsonProperty("components") List<TreatmentOptionComponent.Id> components
    )
    {
        return new TreatmentOptionComponentsChanged(TreatmentOption.Id.of(identity), new LinkedHashSet<>(components));
    }
}
