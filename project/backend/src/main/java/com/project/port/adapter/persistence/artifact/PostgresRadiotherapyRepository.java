package com.project.port.adapter.persistence.artifact;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toSet;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import javax.annotation.Nonnull;

import com.project.domain.classification.radiotherapy.Radiotherapy;
import com.project.domain.classification.radiotherapy.RadiotherapyRepository;
import com.project.domain.event.DomainEvent;
import com.project.port.adapter.persistence.PostgresEntityRepository;
import org.springframework.stereotype.Repository;

@Repository
public class PostgresRadiotherapyRepository implements RadiotherapyRepository
{

    private static final Function<List<DomainEvent>, Radiotherapy> ENTITY_MAPPER_FROM_EVENTS = Radiotherapy::fromEvents;

    @Nonnull
    private final PostgresEntityRepository postgresEntityRepository;


    public PostgresRadiotherapyRepository(
        @Nonnull PostgresEntityRepository postgresEntityRepository)
    {
        this.postgresEntityRepository = postgresEntityRepository;
    }

    @Nonnull
    @Override
    public Optional<Radiotherapy> ofId(@Nonnull Radiotherapy.Id identity)
    {

        return this.postgresEntityRepository.entityOfId(
            identity.asText(),
            Radiotherapy.class,
            ENTITY_MAPPER_FROM_EVENTS
        );
    }

    @Override
    public void save(@Nonnull Radiotherapy radiotherapy)
    {
        this.postgresEntityRepository.save(radiotherapy.state(), Radiotherapy.class, radiotherapy.radiotherapyId().asText());
        radiotherapy.state().clearAccumulatedEvents();
    }

    @Override
    @Nonnull
    public Collection<Radiotherapy> ofIds(@Nonnull Set<Radiotherapy.Id> drugIds)
    {
        return drugIds.isEmpty() ? emptyList() : drugIds.stream()
            .map(this::ofId)
            .filter(Optional::isPresent)
            .map(Optional::orElseThrow)
            .collect(toSet());
    }

    @Nonnull
    @Override
    public Collection<Radiotherapy> all()
    {
        return this.postgresEntityRepository.findAll(
            Radiotherapy.class,
            ENTITY_MAPPER_FROM_EVENTS
        );
    }
}
