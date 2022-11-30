package com.project.domain.classification.drug;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.annotation.Nonnull;

import com.project.domain.classification.Artifact;
import com.project.domain.classification.therapyclass.ArtifactClass;
import com.project.domain.event.DomainEvent;
import com.project.domain.treatmentoption.TreatmentOptionComponent;
import com.project.domain.classification.AggregateRootId;
import com.project.domain.classification.CaseInsensitiveName;

public final class Drug implements Artifact
{

    private final DrugState state;

    @Nonnull
    public static Drug fromEvents(@Nonnull List<DomainEvent> events)
    {
        return new Drug(events);
    }

    private Drug(@Nonnull List<DomainEvent> events)
    {
        this.state = new DrugState(events);
    }

    public DrugState state()
    {
        return this.state;
    }

    @Nonnull
    public Name canonicalName()
    {
        return this.state.canonicalName();
    }

    @Nonnull
    public Drug.Id drugId()
    {
        return this.state.drugId();
    }

    @Nonnull
    @Override
    public String identity()
    {
        return this.state.drugId().asText();
    }

    @Nonnull
    @Override
    public Optional<ArtifactClass.Id> artifactClass()
    {
        return this.state.therapyClasses();
    }

    @Nonnull
    @Override
    public Set<Name> names()
    {
        return this.state.names();
    }

    @Nonnull
    public Set<Name> alternativeNames()
    {
        return this.state.alternativeNames();
    }

    @Nonnull
    public Optional<String> description()
    {
        return this.state.description();
    }

    @Nonnull
    @Override
    public Class<? extends TreatmentOptionComponent> entityType()
    {
        return Drug.class;
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
        Drug that = (Drug) o;
        return this.hasSameIdentityAs(that);
    }

    @Override
    public int hashCode()
    {
        return hashCodeByIdentity();
    }
}
