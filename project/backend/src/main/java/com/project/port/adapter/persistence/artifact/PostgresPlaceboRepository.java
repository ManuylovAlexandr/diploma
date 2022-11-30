package com.project.port.adapter.persistence.artifact;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toSet;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import javax.annotation.Nonnull;

import com.project.domain.classification.placebo.Placebo;
import com.project.domain.classification.placebo.PlaceboRepository;
import com.project.domain.classification.radiotherapy.Radiotherapy;
import com.project.domain.event.DomainEvent;
import com.project.port.adapter.persistence.PostgresEntityRepository;
import org.springframework.stereotype.Repository;

@Repository
public class PostgresPlaceboRepository implements PlaceboRepository
{

    private static final Function<List<DomainEvent>, Placebo> ENTITY_MAPPER_FROM_EVENTS = Placebo::fromEvents;

    @Nonnull
    private final PostgresEntityRepository postgresEntityRepository;

    public PostgresPlaceboRepository(
        @Nonnull PostgresEntityRepository postgresEntityRepository)
    {
        this.postgresEntityRepository = postgresEntityRepository;
    }

    @Nonnull
    @Override
    public Optional<Placebo> ofId(@Nonnull Placebo.Id identity)
    {

        return this.postgresEntityRepository.entityOfId(
            identity.asText(),
            Radiotherapy.class,
            ENTITY_MAPPER_FROM_EVENTS
        );
    }

    @Override
    public void save(@Nonnull Placebo placebo)
    {
        this.postgresEntityRepository.save(placebo.state(), Placebo.class, placebo.placeboId().asText());
        placebo.state().clearAccumulatedEvents();
    }

    @Nonnull
    @Override
    public Collection<Placebo> ofIds(@Nonnull Set<Placebo.Id> identities)
    {
        return identities.isEmpty() ? emptyList() : identities.stream()
            .map(this::ofId)
            .filter(Optional::isPresent)
            .map(Optional::orElseThrow)
            .collect(toSet());
    }

    @Nonnull
    @Override
    public Collection<Placebo> all()
    {
        return this.postgresEntityRepository.findAll(
            Placebo.class,
            ENTITY_MAPPER_FROM_EVENTS
        );
    }
}
