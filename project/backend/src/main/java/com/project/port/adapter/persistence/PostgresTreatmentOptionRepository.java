package com.project.port.adapter.persistence;

import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toSet;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import com.project.domain.classification.therapyclass.ArtifactRepository;
import com.project.domain.event.DomainEvent;
import com.project.domain.treatmentoption.TreatmentOption;
import com.project.domain.treatmentoption.TreatmentOptionComponent;
import com.project.domain.treatmentoption.TreatmentOptionRepository;
import com.project.port.adapter.persistence.PostgresEntityRepository;
import org.springframework.stereotype.Repository;

@Repository
public class PostgresTreatmentOptionRepository implements TreatmentOptionRepository
{

    private static final Function<List<DomainEvent>, TreatmentOption> ENTITY_MAPPER_FROM_EVENTS = TreatmentOption::fromEvents;

    private static final String TREATMENT_OPTION_TYPE = "TreatmentOption";

    @Nonnull
    private final PostgresEntityRepository entityRepository;

    @Nonnull
    private final Map<String, ArtifactRepository<?, ?>> artifactRepositories;

    public PostgresTreatmentOptionRepository(
        @Nonnull PostgresEntityRepository entityRepository,
        @Nonnull List<ArtifactRepository<?, ?>> artifactRepositories)
    {
        this.entityRepository = entityRepository;
        this.artifactRepositories = artifactRepositories.stream().collect(Collectors.toMap(
            (ArtifactRepository<?, ?> artifactRepository) -> artifactRepository.artifactType().getSimpleName(),
            Function.identity()
        ));
    }

    @Override
    public void save(@Nonnull TreatmentOption treatmentOption)
    {

        this.entityRepository.save(treatmentOption.state(), TreatmentOption.class, treatmentOption.identity());
        treatmentOption.state().clearAccumulatedEvents();
    }

    @Nonnull
    @Override
    public Optional<TreatmentOption> ofId(@Nonnull TreatmentOption.Id treatmentOptionId)
    {

        return this.entityRepository.entityOfId(
            treatmentOptionId.asText(),
            TreatmentOption.class,
            ENTITY_MAPPER_FROM_EVENTS
        );
    }

    @Nonnull
    @Override
    public Collection<TreatmentOption> ofIds(@Nonnull Set<TreatmentOption.Id> ids)
    {
        return ids.isEmpty()
            ? emptySet()
            : all().stream()
                .filter(treatmentOption -> ids.contains(treatmentOption.treatmentOptionId()))
                .collect(toSet());
    }

    @Nonnull
    @Override
    public Set<TreatmentOption> all()
    {
        return this.entityRepository.findAll(TreatmentOption.class, ENTITY_MAPPER_FROM_EVENTS).stream()
            .collect(Collectors.toUnmodifiableSet());
    }

    @Nonnull
    @Override
    public Optional<? extends TreatmentOptionComponent> componentOfId(@Nonnull TreatmentOptionComponent.Id componentId)
    {
        return componentId.type().equals(TREATMENT_OPTION_TYPE)
            ? ofId(TreatmentOption.Id.of(componentId.identity()))
            : this.artifactRepositories.get(componentId.type()).ofStringId(componentId.identity());
    }

    @Nonnull
    private TreatmentOptionComponent fetchComponent(@Nonnull TreatmentOptionComponent.Id componentId)
    {
        var optionalComponent = this.componentOfId(componentId);
        return optionalComponent.orElseThrow(() -> new IllegalStateException(
            "Treatment option can't contain as component not existing entity: " + componentId.identity())
        );
    }

    @Nonnull
    private Set<TreatmentOptionComponent> allComponents(@Nonnull Set<TreatmentOptionComponent> acc)
    {
        var nextLevelComponents = acc.stream()
            .map(treatmentOptionComponent -> componentsOfExistingTreatmentOption(
                (TreatmentOption) treatmentOptionComponent)
            )
            .flatMap(Collection::stream)
            .collect(toSet());
        var nextLevelCompositeComponents = nextLevelComponents.stream()
            .filter(TreatmentOptionComponent::isComposite)
            .collect(toSet());
        if (!nextLevelCompositeComponents.isEmpty())
        {
            var nextLevelComponentsChildren = allComponents(nextLevelCompositeComponents);
            acc.addAll(nextLevelComponentsChildren);
        }
        acc.addAll(nextLevelComponents);
        return acc;
    }

    @Nonnull
    private Set<TreatmentOptionComponent> componentsOfExistingTreatmentOption(
        @Nonnull TreatmentOption treatmentOption)
    {
        return treatmentOption.components().stream()
            .map(this::fetchComponent)
            .collect(toSet());
    }
}
