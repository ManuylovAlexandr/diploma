package com.project.domain.classification.drug.event;


import javax.annotation.Nonnull;

import com.project.domain.classification.drug.Drug;
import com.project.domain.event.DomainEvent;

public abstract class DrugEvent extends DomainEvent
{

    @Nonnull
    private final String drugId;

    protected DrugEvent(@Nonnull Drug.Id drugId)
    {
        this.drugId = drugId.asText();
    }

    @Nonnull
    public Drug.Id drugId()
    {
        return new Drug.Id(this.drugId);
    }

    @Nonnull
    @Override
    public String streamId()
    {
        return this.drugId;
    }
}
