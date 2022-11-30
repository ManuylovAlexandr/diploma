package com.project.domain.classification.therapyclass;

import java.util.List;

import javax.annotation.Nonnull;

import com.project.domain.event.DomainEvent;

public final class TherapyClass extends ArtifactClassState implements ArtifactClass
{

    @Nonnull
    public static TherapyClass fromEvents(@Nonnull List<DomainEvent> events)
    {
        return new TherapyClass(events);
    }

    private TherapyClass(@Nonnull List<DomainEvent> events)
    {
        super(events);
    }
}
