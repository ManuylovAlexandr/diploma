package com.project.domain.treatmentoption;

import javax.annotation.Nonnull;

public enum TreatmentOptionRelationToComponents
{

    SUBSUMES("Subsumes"),
    HAS_PART("Has_Part");

    @Nonnull
    private final String relationToComponents;

    TreatmentOptionRelationToComponents(@Nonnull String relationToComponents)
    {
        this.relationToComponents = relationToComponents;
    }

    @Nonnull
    public String relationToComponents()
    {
        return this.relationToComponents;
    }
}
