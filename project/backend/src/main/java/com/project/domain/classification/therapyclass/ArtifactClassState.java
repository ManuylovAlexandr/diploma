package com.project.domain.classification.therapyclass;

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
import com.project.domain.classification.therapyclass.event.AlternativeNamesChanged;
import com.project.domain.classification.therapyclass.event.CanonicalNameChanged;
import com.project.domain.classification.therapyclass.event.DescriptionChanged;
import com.project.domain.classification.therapyclass.event.NewAdded;
import com.project.domain.classification.therapyclass.event.SuperClassChanged;
import com.project.domain.event.DomainEvent;
import com.project.domain.classification.AggregateRoot;

public class ArtifactClassState extends AggregateRoot<DomainEvent>
{

    private static final BiConsumer<DomainEvent, ArtifactClassState> EVENTS_APPLIER =
        CompoundTypeSafeBiConsumer.<DomainEvent, ArtifactClassState>builder()
            .withConsumer(
                NewAdded.class,
                (event, state) -> state.applyNewAdded(event)
            )
            .withConsumer(
                CanonicalNameChanged.class,
                (event, state) -> state.setCanonicalName(event.canonicalName())
            )
            .withConsumer(
                AlternativeNamesChanged.class,
                (event, state) -> state.setAlternativeNames(event.alternativeNames())
            )
            .withConsumer(
                DescriptionChanged.class,
                (event, state) -> state.setDescription(event.description().orElse(null))
            )
            .withConsumer(
                SuperClassChanged.class,
                (event, state) -> state.setSuperClasses(event.superClass())
            )
            .build();

    private ArtifactClass.Id identity;

    private ArtifactClass.Name canonicalName;

    private ArtifactClassTypeTag classTypeTag;

    @Nonnull
    private Set<ArtifactClass.Name> alternativeNames = emptySet();

    @Nullable
    private String description;

    @Nullable
    private ArtifactClass.Id superClass;

    protected ArtifactClassState(@Nonnull List<DomainEvent> events)
    {
        events.forEach(this::applyEvent);
    }

    @Nonnull
    public final ArtifactClass.Id identity()
    {
        return this.identity;
    }

    @Nonnull
    public final ArtifactClass.Name canonicalName()
    {
        return this.canonicalName;
    }

    @Nonnull
    public final ArtifactClassTypeTag classTypeTag()
    {
        return this.classTypeTag;
    }

    @Nonnull
    public final Set<ArtifactClass.Name> alternativeNames()
    {
        return unmodifiableSet(this.alternativeNames);
    }

    @Nonnull
    public final Set<ArtifactClass.Name> names()
    {
        return Stream.of(singleton(this.canonicalName), this.alternativeNames)
            .flatMap(Collection::stream)
            .collect(toSet());
    }

    @Nonnull
    public final Optional<String> description()
    {
        return Optional.ofNullable(this.description);
    }

    @Override
    protected void applyEvent(DomainEvent event)
    {

        EVENTS_APPLIER.accept(event, this);
    }

    private void setIdentity(@Nonnull ArtifactClass.Id identity)
    {
        this.identity = identity;
    }

    private void setClassTypeTag(@Nonnull ArtifactClassTypeTag classTypeTag)
    {
        this.classTypeTag = classTypeTag;
    }

    private void setCanonicalName(@Nonnull ArtifactClass.Name canonicalName)
    {
        this.canonicalName = canonicalName;
    }

    private void setAlternativeNames(
        @Nonnull Set<ArtifactClass.Name> alternativeNames)
    {
        this.alternativeNames = alternativeNames;
    }

    private void setDescription(@Nullable String description)
    {
        this.description = description;
    }

    private void setSuperClasses(@Nullable ArtifactClass.Id superClasses)
    {
        this.setSuperClass(superClasses);
    }

    private void setSuperClass(@Nullable ArtifactClass.Id superClass)
    {
        this.superClass = superClass;
    }

    private void applyNewAdded(@Nonnull NewAdded newAdded)
    {
        setIdentity(newAdded.identity());
        setClassTypeTag(ArtifactClassTypeTag.classTypeTagOfArtifactClass(newAdded.artifactClassType()));
        setCanonicalName(newAdded.canonicalName());
        setSuperClasses(newAdded.superClass().orElse(null));
    }
}
