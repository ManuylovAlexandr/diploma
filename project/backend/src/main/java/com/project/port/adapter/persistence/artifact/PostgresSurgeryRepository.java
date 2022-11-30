package com.project.port.adapter.persistence.artifact;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toSet;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import javax.annotation.Nonnull;

import com.project.domain.classification.surgery.Surgery;
import com.project.domain.classification.surgery.SurgeryRepository;
import com.project.domain.event.DomainEvent;
import com.project.port.adapter.persistence.PostgresEntityRepository;
import org.springframework.stereotype.Repository;

@Repository
public class PostgresSurgeryRepository implements SurgeryRepository
{

    private static final Function<List<DomainEvent>, Surgery> ENTITY_MAPPER_FROM_EVENTS = Surgery::fromEvents;


    @Nonnull
    private final PostgresEntityRepository postgresEntityViewRepository;

    public PostgresSurgeryRepository(
        @Nonnull PostgresEntityRepository postgresEntityViewRepository)
    {
        this.postgresEntityViewRepository = postgresEntityViewRepository;
    }

    @Nonnull
    @Override
    public Optional<Surgery> ofId(@Nonnull Surgery.Id identity)
    {

        return this.postgresEntityViewRepository.entityOfId(
            identity.asText(),
            Surgery.class,
            ENTITY_MAPPER_FROM_EVENTS
        );
    }

    @Override
    public void save(@Nonnull Surgery surgery)
    {
        this.postgresEntityViewRepository.save(surgery.state(), Surgery.class, surgery.surgeryId().asText());
        surgery.state().clearAccumulatedEvents();
    }

    @Override
    @Nonnull
    public Collection<Surgery> ofIds(@Nonnull Set<Surgery.Id> drugIds)
    {
        return drugIds.isEmpty() ? emptyList() : drugIds.stream()
            .map(this::ofId)
            .filter(Optional::isPresent)
            .map(Optional::orElseThrow)
            .collect(toSet());
    }

    @Nonnull
    @Override
    public Collection<Surgery> all()
    {
        return this.postgresEntityViewRepository.findAll(
            Surgery.class,
            ENTITY_MAPPER_FROM_EVENTS
        );
    }
}
