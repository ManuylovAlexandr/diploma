package com.project.domain.treatmentoption;

import static java.util.Collections.emptySet;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.project.common.CompoundTypeSafeBiConsumer;
import com.project.domain.event.DomainEvent;
import com.project.domain.treatmentoption.event.NewTreatmentOptionAdded;
import com.project.domain.treatmentoption.event.TreatmentOptionAlternativeNamesChanged;
import com.project.domain.treatmentoption.event.TreatmentOptionCanonicalNameChanged;
import com.project.domain.treatmentoption.event.TreatmentOptionComponentsChanged;
import com.project.domain.treatmentoption.event.TreatmentOptionDescriptionChanged;
import com.project.domain.treatmentoption.event.TreatmentOptionRelationToComponentsChanged;
import com.project.domain.classification.AggregateRoot;

public class TreatmentOptionState extends AggregateRoot<DomainEvent>
{

    private static final BiConsumer<DomainEvent, TreatmentOptionState> EVENTS_APPLIER =
        CompoundTypeSafeBiConsumer.<DomainEvent, TreatmentOptionState>builder()
            .withConsumer(
                NewTreatmentOptionAdded.class,
                (event, state) -> state.applyNewAdded(event)
            )
            .withConsumer(
                TreatmentOptionCanonicalNameChanged.class,
                (event, state) -> state.setCanonicalName(event.canonicalName())
            )
            .withConsumer(
                TreatmentOptionAlternativeNamesChanged.class,
                (event, state) -> state.setAlternativeNames(event.alternativeNames())
            )
            .withConsumer(
                TreatmentOptionDescriptionChanged.class,
                (event, state) -> state.setDescription(event.description().orElse(null))
            )
            .withConsumer(
                TreatmentOptionComponentsChanged.class,
                (event, state) -> state.setComponents(event.components())
            )
            .withConsumer(
                TreatmentOptionRelationToComponentsChanged.class,
                (event, state) -> state.setRelationToComponents(event.relationToComponents())
            )
            .build();

    private TreatmentOption.Id treatmentOptionId;

    private TreatmentOption.Name canonicalName;

    private Set<TreatmentOption.Name> alternativeNames = emptySet();

    private String description;

    private Set<TreatmentOptionComponent.Id> components = emptySet();

    private TreatmentOptionRelationToComponents relationToComponents;

    protected TreatmentOptionState(@Nonnull List<DomainEvent> events)
    {

        events.forEach(this::applyEventWithMeta);
    }

    @Override
    protected void applyEvent(DomainEvent event)
    {

        EVENTS_APPLIER.accept(event, this);
    }

    @Nonnull
    public TreatmentOption.Id treatmentOptionId()
    {
        return this.treatmentOptionId;
    }

    @Nonnull
    public TreatmentOption.Name canonicalName()
    {
        return this.canonicalName;
    }

    @Nonnull
    public Set<TreatmentOption.Name> alternativeNames()
    {
        return Collections.unmodifiableSet(this.alternativeNames);
    }

    @Nonnull
    public Optional<String> description()
    {
        return Optional.ofNullable(this.description);
    }


    @Nonnull
    public Set<TreatmentOptionComponent.Id> components()
    {
        return Collections.unmodifiableSet(this.components);
    }

    private void applyNewAdded(NewTreatmentOptionAdded event)
    {

        this.setTreatmentOptionId(event.treatmentOptionId());
        this.setCanonicalName(event.canonicalName());
        this.setComponents(event.components());
    }

    private void setTreatmentOptionId(@Nonnull TreatmentOption.Id treatmentOptionId)
    {
        this.treatmentOptionId = treatmentOptionId;
    }

    private void setCanonicalName(@Nonnull TreatmentOption.Name canonicalName)
    {
        this.canonicalName = canonicalName;
    }

    private void setAlternativeNames(@Nonnull Set<TreatmentOption.Name> alternativeNames)
    {
        this.alternativeNames = alternativeNames;
    }

    private void setDescription(@Nullable String description)
    {
        this.description = description;
    }

    private void setComponents(@Nonnull Set<TreatmentOptionComponent.Id> components)
    {
        this.components = components;
    }

    private void setRelationToComponents(@Nonnull TreatmentOptionRelationToComponents relationToComponents)
    {
        this.relationToComponents = relationToComponents;
    }
}
