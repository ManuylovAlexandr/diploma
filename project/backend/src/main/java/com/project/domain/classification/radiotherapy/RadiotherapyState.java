package com.project.domain.classification.radiotherapy;

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
import com.project.domain.classification.radiotherapy.event.NewRadiotherapyAdded;
import com.project.domain.classification.radiotherapy.event.RadiotherapyAlternativeNamesChanged;
import com.project.domain.classification.radiotherapy.event.RadiotherapyCanonicalNameChanged;
import com.project.domain.classification.radiotherapy.event.RadiotherapyDescriptionChanged;
import com.project.domain.classification.radiotherapy.event.RadiotherapyParentClassChanged;
import com.project.domain.event.DomainEvent;
import com.project.domain.classification.AggregateRoot;

public class RadiotherapyState extends AggregateRoot<DomainEvent>
{

    private static final BiConsumer<DomainEvent, RadiotherapyState> EVENTS_APPLIER =
        CompoundTypeSafeBiConsumer.<DomainEvent, RadiotherapyState>builder()
            .withConsumer(
                NewRadiotherapyAdded.class,
                (event, state) -> state.applyNewAdded(event)
            )
            .withConsumer(
                RadiotherapyCanonicalNameChanged.class,
                (event, state) -> state.setCanonicalName(event.canonicalName())
            )
            .withConsumer(
                RadiotherapyAlternativeNamesChanged.class,
                (event, state) -> state.setAlternativeNames(event.alternativeNames())
            )
            .withConsumer(
                RadiotherapyParentClassChanged.class,
                (event, state) -> state.setParentClass(event.parentClass().orElse(null))
            )
            .withConsumer(
                RadiotherapyDescriptionChanged.class,
                (event, state) -> state.setDescription(event.description().orElse(null))
            )
            .build();

    private Radiotherapy.Id radiotherapyId;

    private Radiotherapy.Name canonicalName;

    private Set<Radiotherapy.Name> alternativeNames = emptySet();

    private ArtifactClass.Id parentClassId;

    private String description;

    protected RadiotherapyState(@Nonnull List<DomainEvent> events)
    {


        events.forEach(this::applyEvent);
    }

    public Radiotherapy.Id radiotherapyId()
    {
        return this.radiotherapyId;
    }

    public Radiotherapy.Name canonicalName()
    {
        return this.canonicalName;
    }

    public Set<Radiotherapy.Name> alternativeNames()
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

    private void applyNewAdded(NewRadiotherapyAdded event)
    {
        this.setRadiotherapyId(event.identity());
        this.setCanonicalName(event.canonicalName());
    }

    private void setRadiotherapyId(@Nonnull String radiotherapyId)
    {
        this.radiotherapyId = new Radiotherapy.Id(radiotherapyId);
    }

    private void setCanonicalName(@Nonnull String canonicalName)
    {
        this.canonicalName = new Radiotherapy.Name(canonicalName);
    }

    private void setAlternativeNames(@Nonnull Set<String> alternativeNames)
    {
        this.alternativeNames = alternativeNames.stream().map(Radiotherapy.Name::new).collect(Collectors.toSet());
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
    public Set<Radiotherapy.Name> names()
    {
        return Stream.concat(Stream.of(this.canonicalName), this.alternativeNames.stream()).collect(toSet());
    }
}
