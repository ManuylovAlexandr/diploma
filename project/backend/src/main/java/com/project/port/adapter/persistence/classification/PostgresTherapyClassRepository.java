package com.project.port.adapter.persistence.classification;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toSet;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import javax.annotation.Nonnull;

import com.project.domain.classification.therapyclass.ArtifactClass;
import com.project.domain.classification.therapyclass.ArtifactClassRepository;
import com.project.domain.classification.therapyclass.TherapyClass;
import com.project.domain.event.DomainEvent;
import com.project.port.adapter.persistence.PostgresEntityRepository;
import org.springframework.stereotype.Repository;

@Repository
public class PostgresTherapyClassRepository implements ArtifactClassRepository<TherapyClass>
{

    private static final Function<List<DomainEvent>, TherapyClass>
        ENTITY_MAPPER_FROM_EVENTS = TherapyClass::fromEvents;

    @Nonnull
    private final PostgresEntityRepository postgresEntityRepository;

    public PostgresTherapyClassRepository(
        @Nonnull PostgresEntityRepository postgresEntityRepository
    )
    {
        this.postgresEntityRepository = postgresEntityRepository;
    }

    @Nonnull
    @Override
    public Optional<TherapyClass> ofId(@Nonnull ArtifactClass.Id therapyClassId)
    {
        return this.postgresEntityRepository.entityOfId(
            therapyClassId.asText(),
            TherapyClass.class,
            ENTITY_MAPPER_FROM_EVENTS
        );
    }

    @Nonnull
    public Collection<TherapyClass> artifactClassOfIds(@Nonnull Set<ArtifactClass.Id> artifactIds)
    {
        return artifactIds.isEmpty() ? emptyList() : artifactIds.stream()
            .map(this::ofId)
            .filter(Optional::isPresent)
            .map(Optional::orElseThrow)
            .collect(toSet());
    }

    @Override
    public void save(@Nonnull TherapyClass therapyClass)
    {
        this.postgresEntityRepository.save(therapyClass, TherapyClass.class, therapyClass.identity().asText());
        therapyClass.clearAccumulatedEvents();
    }

    @Override
    @Nonnull
    public Collection<TherapyClass> all()
    {
        return this.postgresEntityRepository.findAll(
            TherapyClass.class,
            ENTITY_MAPPER_FROM_EVENTS
        );
    }
}
