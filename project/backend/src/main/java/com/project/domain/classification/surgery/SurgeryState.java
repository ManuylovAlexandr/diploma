package com.project.domain.classification.surgery;

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
import com.project.domain.classification.surgery.event.NewSurgeryAdded;
import com.project.domain.classification.surgery.event.SurgeryAlternativeNamesChanged;
import com.project.domain.classification.surgery.event.SurgeryCanonicalNameChanged;
import com.project.domain.classification.surgery.event.SurgeryDescriptionChanged;
import com.project.domain.classification.surgery.event.SurgeryParentClassChanged;
import com.project.domain.event.DomainEvent;
import com.project.domain.classification.AggregateRoot;

public class SurgeryState extends AggregateRoot<DomainEvent>
{

    private static final BiConsumer<DomainEvent, SurgeryState> EVENTS_APPLIER =
        CompoundTypeSafeBiConsumer.<DomainEvent, SurgeryState>builder()
            .withConsumer(
                NewSurgeryAdded.class,
                (event, state) -> state.applyNewAdded(event)
            )
            .withConsumer(
                SurgeryCanonicalNameChanged.class,
                (event, state) -> state.setCanonicalName(event.canonicalName())
            )
            .withConsumer(
                SurgeryAlternativeNamesChanged.class,
                (event, state) -> state.setAlternativeNames(event.alternativeNames())
            )
            .withConsumer(
                SurgeryParentClassChanged.class,
                (event, state) -> state.setParentClass(event.parentClass().orElse(null))
            )
            .withConsumer(
                SurgeryDescriptionChanged.class,
                (event, state) -> state.setDescription(event.description().orElse(null))
            )
            .build();

    private Surgery.Id surgeryId;

    private Surgery.Name canonicalName;

    private Set<Surgery.Name> alternativeNames = emptySet();

    private ArtifactClass.Id parentClassId;

    private String description;

    protected SurgeryState(@Nonnull List<DomainEvent> events)
    {


        events.forEach(this::applyEvent);
    }

    public Surgery.Id surgeryId()
    {
        return this.surgeryId;
    }

    public Surgery.Name canonicalName()
    {
        return this.canonicalName;
    }

    public Set<Surgery.Name> alternativeNames()
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

    private void applyNewAdded(NewSurgeryAdded event)
    {
        this.setSurgeryId(event.identity());
        this.setCanonicalName(event.canonicalName());
    }

    private void setSurgeryId(@Nonnull String surgeryId)
    {
        this.surgeryId = new Surgery.Id(surgeryId);
    }

    private void setCanonicalName(@Nonnull String canonicalName)
    {
        this.canonicalName = new Surgery.Name(canonicalName);
    }

    private void setAlternativeNames(@Nonnull Set<String> alternativeNames)
    {
        this.alternativeNames = alternativeNames.stream().map(Surgery.Name::new).collect(Collectors.toSet());
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
    public Set<Surgery.Name> names()
    {
        return Stream.concat(Stream.of(this.canonicalName), this.alternativeNames.stream()).collect(toSet());
    }
}
