package com.project.domain.event;

import java.util.Objects;

import javax.annotation.Nonnull;

public class VersionedEventType
{

    private final int eventVersion;

    private final Class<? extends DomainEvent> eventType;

    public VersionedEventType(
        int eventVersion,
        @Nonnull Class<? extends DomainEvent> eventType)
    {
        this.eventVersion = eventVersion;
        this.eventType = eventType;
    }

    public int eventVersion()
    {
        return this.eventVersion;
    }

    @Nonnull
    public Class<? extends DomainEvent> eventType()
    {
        return this.eventType;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(this.eventVersion, this.eventType);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }
        VersionedEventType that = (VersionedEventType) o;
        return this.eventVersion == that.eventVersion &&
            Objects.equals(this.eventType, that.eventType);
    }

    @Override
    public String toString()
    {
        return "VersionedEventType{" +
            "eventVersion=" + eventVersion +
            ", eventType=" + eventType +
            '}';
    }
}
