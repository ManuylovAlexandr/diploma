package com.project.port.adapter.semantic.projection;


import java.util.function.Function;

import javax.annotation.Nonnull;

import com.project.domain.event.DomainEvent;
import com.project.domain.event.StoredEvent;
import com.project.domain.event.StoredEventDeserializer;
import com.project.port.adapter.common.projections.ProjectionMaintainingStoredEventHandler;
import com.project.port.adapter.semantic.OntologyManager;
import com.project.port.adapter.semantic.changes.entity.BaseProjectEntityChanges;

public abstract class SemanticMaintainer extends ProjectionMaintainingStoredEventHandler
{

    protected final OntologyManager ontologyManager;

    protected SemanticMaintainer(StoredEventDeserializer deserializer, OntologyManager ontologyManager)
    {
        super(deserializer);
        this.ontologyManager = ontologyManager;
    }

    protected final <T extends DomainEvent, E extends BaseProjectEntityChanges<E>> void registerHandler(
        @Nonnull Class<T> eventType,
        @Nonnull Function<? super T, E> eventHandler
    )
    {
        registerHandler(
            eventType,
            (StoredEvent storedEvent) -> eventHandler.apply(this.deserializer.deserializeEvent(storedEvent, eventType))
                .getChanges(this.ontologyManager::applyChanges)
        );
    }
}
