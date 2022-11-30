package com.project.domain.event.deserializers;

import com.project.domain.event.VersionedEventType;

public interface EventUpcaster
{

    IntermediateEventRepresentation upcast(
        IntermediateEventRepresentation intermediateEventRepresentation);

    boolean canUpcast(IntermediateEventRepresentation intermediateEventRepresentation);

    VersionedEventType versionedEventType();
}
