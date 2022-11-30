package com.project.domain.classification.therapyclass.event;

import javax.annotation.Nonnull;

import com.project.domain.classification.therapyclass.ArtifactClass;
import com.project.domain.event.DomainEvent;

public abstract class ArtifactClassEvent extends DomainEvent
{

    @Nonnull
    private final String identity;

    protected ArtifactClassEvent(@Nonnull ArtifactClass.Id identity)
    {
        this.identity = identity.asText();
    }

    @Nonnull
    public ArtifactClass.Id identity()
    {
        return new ArtifactClass.Id(this.identity);
    }

    @Nonnull
    @Override
    public String streamId()
    {
        return this.identity;
    }
}
