package com.project.domain.classification.drug;

import static java.util.Collections.emptySet;
import static java.util.Collections.singleton;
import static java.util.Collections.unmodifiableSet;
import static java.util.stream.Collectors.toSet;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.project.common.CompoundTypeSafeBiConsumer;
import com.project.domain.classification.drug.event.DrugAlternativeNamesChanged;
import com.project.domain.classification.drug.event.DrugCanonicalNameChanged;
import com.project.domain.classification.drug.event.DrugDescriptionChanged;
import com.project.domain.classification.drug.event.DrugTherapyClassChanged;
import com.project.domain.classification.therapyclass.ArtifactClass;
import com.project.domain.classification.drug.event.NewDrugAdded;
import com.project.domain.event.DomainEvent;
import com.project.domain.classification.AggregateRoot;

public class DrugState extends AggregateRoot<DomainEvent>
{

    private static final BiConsumer<DomainEvent, DrugState> EVENTS_APPLIER =
        CompoundTypeSafeBiConsumer.<DomainEvent, DrugState>builder()
            .withConsumer(
                NewDrugAdded.class,
                (event, state) -> state.applyNewDrugAdded(event)
            )
            .withConsumer(
                DrugCanonicalNameChanged.class,
                (event, state) -> state.setCanonicalName(event.canonicalName())
            )
            .withConsumer(
                DrugAlternativeNamesChanged.class,
                (event, state) -> state.setAlternativeNames(event.alternativeNames())
            )
            .withConsumer(
                DrugTherapyClassChanged.class,
                (event, state) -> state.setTherapyClasses(event.therapyClass().orElse(null))
            )
            .withConsumer(
                DrugDescriptionChanged.class,
                (event, state) -> state.setDescription(event.description().orElse(null))
            )
            .build();

    private Drug.Id drugId;

    private Drug.Name canonicalName;

    @Nonnull
    private Set<Drug.Name> alternativeNames = emptySet();

    @Nullable
    private String description;

    @Nullable
    private ArtifactClass.Id therapyClasses;

    protected DrugState(@Nonnull List<DomainEvent> events)
    {

        events.forEach(this::applyEvent);
    }

    @Nonnull
    public final Drug.Id drugId()
    {
        return this.drugId;
    }

    @Nonnull
    public final Drug.Name canonicalName()
    {
        return this.canonicalName;
    }

    @Nonnull
    public Set<Drug.Name> names()
    {
        return Stream.of(singleton(this.canonicalName()), this.alternativeNames())
            .flatMap(Collection::stream)
            .collect(toSet());
    }

    @Nonnull
    public final Set<Drug.Name> alternativeNames()
    {
        return unmodifiableSet(this.alternativeNames);
    }

    @Nonnull
    public final Optional<String> description()
    {
        return Optional.ofNullable(this.description);
    }

    @Nonnull
    public final Optional<ArtifactClass.Id> therapyClasses()
    {
        return Optional.ofNullable(this.therapyClasses);
    }

    @Override
    protected final void applyEvent(DomainEvent event)
    {
        EVENTS_APPLIER.accept(event, this);
    }

    private void setDrugId(@Nonnull Drug.Id drugId)
    {
        this.drugId = drugId;
    }

    private void setCanonicalName(@Nonnull Drug.Name canonicalName)
    {
        this.canonicalName = canonicalName;
    }

    private void setAlternativeNames(@Nonnull Set<Drug.Name> alternativeNames)
    {
        this.alternativeNames = alternativeNames;
    }

    private void setDescription(@Nullable String description)
    {
        this.description = description;
    }

    private void setTherapyClasses(@Nullable ArtifactClass.Id therapyClasses)
    {
        this.therapyClasses = therapyClasses;
    }

    private void applyNewDrugAdded(NewDrugAdded creationEvent)
    {
        setDrugId(creationEvent.drugId());
        setCanonicalName(creationEvent.canonicalName());
    }
}
