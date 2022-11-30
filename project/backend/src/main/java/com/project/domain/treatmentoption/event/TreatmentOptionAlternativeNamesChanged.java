package com.project.domain.treatmentoption.event;

import static com.project.common.CollectionUtils.map;

import java.util.Collections;
import java.util.Set;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.project.domain.classification.IdentifiedDomainEvent;
import com.project.domain.treatmentoption.TreatmentOption;

public final class TreatmentOptionAlternativeNamesChanged extends IdentifiedDomainEvent
{

    @Nonnull
    private final Set<String> alternativeNames;

    public TreatmentOptionAlternativeNamesChanged(
        @Nonnull TreatmentOption.Id identity,
        @Nonnull Set<TreatmentOption.Name> alternativeNames)
    {
        super(identity.asText());
        this.alternativeNames = map(alternativeNames, TreatmentOption.Name::asText);
    }

    @Nonnull
    public Set<TreatmentOption.Name> alternativeNames()
    {
        return Collections.unmodifiableSet(map(this.alternativeNames, TreatmentOption.Name::of));
    }

    @JsonCreator
    protected static TreatmentOptionAlternativeNamesChanged fromJson(
        @JsonProperty("identity") String identity,
        @JsonProperty("alternativeNames") Set<String> alternativeNames)
    {
        return new TreatmentOptionAlternativeNamesChanged(
            TreatmentOption.Id.of(identity),
            map(alternativeNames, TreatmentOption.Name::of)
        );
    }
}
