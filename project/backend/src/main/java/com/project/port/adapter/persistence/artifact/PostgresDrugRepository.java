package com.project.port.adapter.persistence.artifact;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toSet;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import javax.annotation.Nonnull;

import com.project.domain.classification.drug.Drug;
import com.project.domain.classification.drug.DrugRepository;
import com.project.domain.event.DomainEvent;
import com.project.port.adapter.persistence.PostgresEntityRepository;
import org.springframework.stereotype.Repository;

@Repository
public class PostgresDrugRepository implements DrugRepository
{

    private static final Function<List<DomainEvent>, Drug> ENTITY_MAPPER_FROM_EVENTS = Drug::fromEvents;

    @Nonnull
    private final PostgresEntityRepository postgresEntityRepository;

    @Nonnull
    private final PostgresEntityRepository postgresEntityViewRepository;


    public PostgresDrugRepository(
        @Nonnull PostgresEntityRepository postgresEntityRepository,
        @Nonnull PostgresEntityRepository postgresEntityViewRepository)
    {
        this.postgresEntityRepository = postgresEntityRepository;
        this.postgresEntityViewRepository = postgresEntityViewRepository;
    }

    @Override
    @Nonnull
    public Optional<Drug> ofId(@Nonnull Drug.Id drugId)
    {
        return this.postgresEntityViewRepository.entityOfId(
            drugId.asText(),
            Drug.class,
            ENTITY_MAPPER_FROM_EVENTS
        );
    }

    @Override
    @Nonnull
    public Collection<Drug> ofIds(@Nonnull Set<Drug.Id> drugIds)
    {
        return drugIds.isEmpty() ? emptyList() : drugIds.stream()
            .map(this::ofId)
            .filter(Optional::isPresent)
            .map(Optional::orElseThrow)
            .collect(toSet());
    }

    @Override
    public void save(@Nonnull Drug drug)
    {
        this.postgresEntityRepository.save(drug.state(), Drug.class, drug.drugId().asText());
        drug.state().clearAccumulatedEvents();
    }


    @Override
    @Nonnull
    public Collection<Drug> all()
    {
        return this.postgresEntityViewRepository.findAll(
            Drug.class,
            ENTITY_MAPPER_FROM_EVENTS
        );
    }
}
