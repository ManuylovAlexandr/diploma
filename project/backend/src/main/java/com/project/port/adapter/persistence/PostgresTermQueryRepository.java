package com.project.port.adapter.persistence;


import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import org.springframework.stereotype.Repository;

import com.project.domain.event.DomainEvent;
import com.project.domain.termquery.TermQuery;
import com.project.domain.termquery.TermQueryRepository;
import com.project.port.adapter.persistence.PostgresEntityRepository;

@Repository
public class PostgresTermQueryRepository implements TermQueryRepository
{

    private static final Function<List<DomainEvent>, TermQuery> ENTITY_MAPPER_FROM_EVENTS =
        TermQuery::fromEvents;

    private final PostgresEntityRepository entityRepository;

    public PostgresTermQueryRepository(
        PostgresEntityRepository entityRepository)
    {
        this.entityRepository = entityRepository;
    }

    @Override
    public Optional<TermQuery> ofId(@Nonnull TermQuery.Id id)
    {
        return this.entityRepository.entityOfId(id.asText(), TermQuery.class, ENTITY_MAPPER_FROM_EVENTS);
    }

    @Override
    public void save(@Nonnull TermQuery termQuery)
    {
        this.entityRepository.save(termQuery.state(), TermQuery.class, termQuery.id().asText());
        termQuery.state().clearAccumulatedEvents();
    }

    @Nonnull
    @Override
    public Set<TermQuery> all()
    {
        return this.entityRepository.findAll(TermQuery.class, ENTITY_MAPPER_FROM_EVENTS).stream()
            .collect(Collectors.toUnmodifiableSet());
    }
}
