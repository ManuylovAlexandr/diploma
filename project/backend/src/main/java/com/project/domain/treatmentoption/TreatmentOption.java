package com.project.domain.treatmentoption;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

import com.project.domain.event.DomainEvent;
import com.project.domain.classification.AggregateRootId;
import com.project.domain.classification.CaseInsensitiveName;
import com.project.domain.classification.EntityMetadata;

public final class TreatmentOption implements TreatmentOptionComponent
{

    private final TreatmentOptionState state;

    @Nonnull
    public static TreatmentOption fromEvents(@Nonnull List<DomainEvent> events)
    {

        return new TreatmentOption(events);
    }

    private TreatmentOption(@Nonnull List<DomainEvent> events)
    {
        this.state = new TreatmentOptionState(events);
    }

    @Nonnull
    public Id treatmentOptionId()
    {
        return this.state.treatmentOptionId();
    }

    @Nonnull
    @Override
    public String identity()
    {
        return treatmentOptionId().asText();
    }

    @Nonnull
    @Override
    public Class<? extends TreatmentOptionComponent> entityType()
    {
        return TreatmentOption.class;
    }

    @Nonnull
    public Name canonicalName()
    {
        return this.state.canonicalName();
    }

    @Nonnull
    public Set<Name> alternativeNames()
    {
        return this.state.alternativeNames();
    }

    @Nonnull
    public Set<Name> names()
    {
        return Stream.concat(Stream.of(this.state.canonicalName()), this.state.alternativeNames().stream())
            .collect(Collectors.toUnmodifiableSet());
    }

    @Nonnull
    @Override
    public Set<TreatmentOptionComponent.Id> components()
    {
        return this.state.components();
    }

    @Nonnull
    public Optional<String> description()
    {
        return this.state.description();
    }

    @Nonnull
    public EntityMetadata metadata()
    {
        return this.state.metadata();
    }

    @Nonnull
    public TreatmentOptionState state()
    {
        return this.state;
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
        TreatmentOption that = (TreatmentOption) o;
        return this.hasSameIdentityAs(that);
    }

    @Override
    public int hashCode()
    {
        return hashCodeByIdentity();
    }

    public static final class Id extends AggregateRootId
    {

        private Id(@Nonnull String id)
        {
            super(id);
        }

        @Nonnull
        public static Id of(@Nonnull String identity)
        {
            return new Id(identity);
        }
    }

    public static final class Name extends CaseInsensitiveName
    {

        private Name(@Nonnull String name)
        {
            super(name);
        }

        @Nonnull
        public static Name of(@Nonnull String text)
        {
            return new Name(text);
        }
    }
}
