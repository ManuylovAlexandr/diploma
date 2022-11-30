package com.project.domain.classification.radiotherapy;

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

public final class Radiotherapy implements Artifact
{

    private final RadiotherapyState state;

    @Nonnull
    public static Radiotherapy fromEvents(@Nonnull List<DomainEvent> events)
    {
        return new Radiotherapy(events);
    }

    private Radiotherapy(@Nonnull List<DomainEvent> events)
    {
        this.state = new RadiotherapyState(events);
    }

    @Nonnull
    @Override
    public String identity()
    {
        return this.state.radiotherapyId().asText();
    }

    @Nonnull
    @Override
    public Class<? extends TreatmentOptionComponent> entityType()
    {
        return Radiotherapy.class;
    }

    public Radiotherapy.Id radiotherapyId()
    {
        return this.state.radiotherapyId();
    }

    public Set<Radiotherapy.Name> names()
    {
        return this.state.names();
    }

    @Nonnull
    @Override
    public Radiotherapy.Name canonicalName()
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

    public RadiotherapyState state()
    {
        return this.state;
    }

    @Nonnull
    public Optional<String> description()
    {
        return this.state.description();
    }

    @Nonnull
    public Set<Radiotherapy.Name> alternativeNames()
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
        Radiotherapy that = (Radiotherapy) o;
        return this.hasSameIdentityAs(that);
    }

    @Override
    public int hashCode()
    {
        return hashCodeByIdentity();
    }
}