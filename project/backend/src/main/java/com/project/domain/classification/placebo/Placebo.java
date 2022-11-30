package com.project.domain.classification.placebo;

import static java.util.Collections.unmodifiableSet;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.annotation.Nonnull;

import com.project.domain.classification.AggregateRootId;
import com.project.domain.classification.Artifact;
import com.project.domain.classification.CaseInsensitiveName;
import com.project.domain.classification.therapyclass.ArtifactClass;
import com.project.domain.event.DomainEvent;
import com.project.domain.treatmentoption.TreatmentOptionComponent;

public class Placebo implements Artifact
{

    private final PlaceboState state;

    @Nonnull
    public static Placebo fromEvents(@Nonnull List<DomainEvent> events)
    {
        return new Placebo(events);
    }

    private Placebo(@Nonnull List<DomainEvent> events)
    {
        this.state = new PlaceboState(events);
    }

    @Nonnull
    @Override
    public String identity()
    {
        return this.state.placeboId().asText();
    }

    @Nonnull
    @Override
    public Class<? extends TreatmentOptionComponent> entityType()
    {
        return Placebo.class;
    }

    public Placebo.Id placeboId()
    {
        return this.state.placeboId();
    }

    public Set<Placebo.Name> names()
    {
        return this.state.names();
    }

    @Nonnull
    @Override
    public Placebo.Name canonicalName()
    {
        return this.state.canonicalName();
    }

    @Nonnull
    @Override
    public Optional<ArtifactClass.Id> artifactClass()
    {
        return this.state.parentClass();
    }

    public static final class Id extends AggregateRootId
    {

        public Id(@Nonnull String id)
        {
            super(id);
        }
    }

    public static final class Name extends CaseInsensitiveName
    {

        public Name(@Nonnull String name)
        {
            super(name);
        }
    }


    public void clearAccumulatedEvents()
    {
        this.state.clearAccumulatedEvents();
    }

    public PlaceboState state()
    {
        return this.state;
    }

    @Nonnull
    public final Optional<String> description()
    {
        return this.state.description();
    }

    @Nonnull
    public final Set<Placebo.Name> alternativeNames()
    {
        return unmodifiableSet(this.state.alternativeNames());
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }
        Placebo that = (Placebo) o;
        return this.hasSameIdentityAs(that);
    }

    @Override
    public int hashCode()
    {
        return hashCodeByIdentity();
    }
}
