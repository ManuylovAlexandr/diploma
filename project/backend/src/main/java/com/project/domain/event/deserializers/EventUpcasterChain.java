package com.project.domain.event.deserializers;

import static java.util.Comparator.comparingInt;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

import com.project.domain.event.VersionedEventType;

public class EventUpcasterChain implements EventUpcaster
{

    @Nonnull
    private final List<EventUpcaster> upcasters;

    public EventUpcasterChain(@Nonnull List<EventUpcaster> upcasters)
    {
        this.upcasters = upcasters;
    }

    @Override
    public IntermediateEventRepresentation upcast(IntermediateEventRepresentation intermediateEventRepresentation)
    {
        return this.upcasters.stream()
            .map(EventUpcasterChain::upcastWithSingleUpcaster)
            .reduce(Function.identity(), Function::andThen)
            .apply(intermediateEventRepresentation);
    }

    @Override
    public boolean canUpcast(IntermediateEventRepresentation intermediateEventRepresentation)
    {
        if (this.upcasters.isEmpty())
        {
            return false;
        }
        int startEventVersion = this.upcasters.get(0).versionedEventType().eventVersion();
        return startEventVersion == intermediateEventRepresentation.eventVersion()
            && Stream.iterate(startEventVersion, x -> x + 1)
            .limit(this.upcasters.size()).toList()
            .equals(this.upcasters.stream().map(u -> u.versionedEventType().eventVersion()).toList());
    }

    @Override
    public VersionedEventType versionedEventType()
    {
        return this.upcasters.stream()
            .min(comparingInt(upcaster -> upcaster.versionedEventType().eventVersion()))
            .map(EventUpcaster::versionedEventType)
            .orElseThrow();
    }

    @Nonnull
    private static Function<IntermediateEventRepresentation, IntermediateEventRepresentation> upcastWithSingleUpcaster(
        @Nonnull EventUpcaster upcaster
    )
    {
        return upcaster::upcast;
    }
}
