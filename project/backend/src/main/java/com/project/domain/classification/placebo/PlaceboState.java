package com.project.domain.classification.placebo;

import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toSet;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.project.common.CompoundTypeSafeBiConsumer;
import com.project.domain.classification.therapyclass.ArtifactClass;
import com.project.domain.classification.placebo.event.NewPlaceboAdded;
import com.project.domain.classification.placebo.event.PlaceboAlternativeNamesChanged;
import com.project.domain.classification.placebo.event.PlaceboCanonicalNameChanged;
import com.project.domain.classification.placebo.event.PlaceboDescriptionChanged;
import com.project.domain.classification.placebo.event.PlaceboParentClassChanged;
import com.project.domain.event.DomainEvent;
import com.project.domain.classification.AggregateRoot;

public class PlaceboState extends AggregateRoot<DomainEvent>
{

    private static final BiConsumer<DomainEvent, PlaceboState> EVENTS_APPLIER =
        CompoundTypeSafeBiConsumer.<DomainEvent, PlaceboState>builder()
            .withConsumer(
                NewPlaceboAdded.class,
                (event, state) -> state.applyNewAdded(event)
            )
            .withConsumer(
                PlaceboCanonicalNameChanged.class,
                (event, state) -> state.setCanonicalName(event.canonicalName())
            )
            .withConsumer(
                PlaceboAlternativeNamesChanged.class,
                (event, state) -> state.setAlternativeNames(event.alternativeNames())
            )
            .withConsumer(
                PlaceboParentClassChanged.class,
                (event, state) -> state.setParentClass(event.parentClass().orElse(null))
            )
            .withConsumer(
                PlaceboDescriptionChanged.class,
                (event, state) -> state.setDescription(event.description().orElse(null))
            )
            .build();

    private Placebo.Id placeboId;

    private Placebo.Name canonicalName;

    private Set<Placebo.Name> alternativeNames = emptySet();

    private ArtifactClass.Id parentClassId;

    private String description;

    protected PlaceboState(@Nonnull List<DomainEvent> events)
    {


        events.forEach(this::applyEvent);
    }

    public Placebo.Id placeboId()
    {
        return this.placeboId;
    }

    public Placebo.Name canonicalName()
    {
        return this.canonicalName;
    }

    public Set<Placebo.Name> alternativeNames()
    {
        return this.alternativeNames;
    }

    public Optional<ArtifactClass.Id> parentClass()
    {
        return Optional.ofNullable(this.parentClassId);
    }

    public Optional<String> description()
    {
        return Optional.ofNullable(this.description);
    }

    @Override
    protected void applyEvent(DomainEvent event)
    {

        EVENTS_APPLIER.accept(event, this);
    }

    private void applyNewAdded(NewPlaceboAdded event)
    {
        this.setPlaceboId(event.identity());
        this.setCanonicalName(event.canonicalName());
    }

    private void setPlaceboId(@Nonnull String placeboId)
    {
        this.placeboId = new Placebo.Id(placeboId);
    }

    private void setCanonicalName(@Nonnull String canonicalName)
    {
        this.canonicalName = new Placebo.Name(canonicalName);
    }

    private void setAlternativeNames(@Nonnull Set<String> alternativeNames)
    {
        this.alternativeNames = alternativeNames.stream().map(Placebo.Name::new).collect(Collectors.toSet());
    }

    private void setParentClass(@Nullable String parentClassId)
    {
        this.parentClassId = Optional.ofNullable(parentClassId).map(ArtifactClass.Id::new).orElse(null);
    }

    private void setDescription(@Nullable String description)
    {
        this.description = description;
    }

    @Nonnull
    public Set<Placebo.Name> names()
    {
        return Stream.concat(Stream.of(this.canonicalName), this.alternativeNames.stream()).collect(toSet());
    }
}
