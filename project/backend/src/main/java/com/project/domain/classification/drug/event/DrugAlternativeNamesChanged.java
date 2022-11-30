package com.project.domain.classification.drug.event;

import static java.util.stream.Collectors.toSet;

import java.util.Set;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.project.domain.classification.drug.Drug;

public final class DrugAlternativeNamesChanged extends DrugEvent
{

    @Nonnull
    private final Set<String> alternativeNames;

    public DrugAlternativeNamesChanged(@Nonnull Drug.Id drugId, @Nonnull Set<Drug.Name> alternativeNames)
    {
        super(drugId);
        this.alternativeNames = alternativeNames
            .stream()
            .map(Drug.Name::asText)
            .collect(toSet());
    }

    @Nonnull
    public Set<Drug.Name> alternativeNames()
    {
        return this.alternativeNames.stream()
            .map(Drug.Name::new)
            .collect(toSet());
    }

    @JsonCreator
    private static DrugAlternativeNamesChanged fromJson(
        @JsonProperty("drugId") String drugId,
        @JsonProperty("alternativeNames") Set<String> alternativeNames)
    {
        return new DrugAlternativeNamesChanged(
            new Drug.Id(drugId),
            alternativeNames.stream()
                .map(Drug.Name::new)
                .collect(toSet())
        );
    }
}
