package com.project.domain.event;


import java.time.OffsetDateTime;

import javax.annotation.Nonnull;

import com.project.common.ObjectSerializer;

public class StoredEvent
{

    private final long id;

    @Nonnull
    private final String streamId;

    @Nonnull
    private final OffsetDateTime occurredOn;

    @Nonnull
    private final String eventTypeName;

    @Nonnull
    private final String eventPayload;

    @Nonnull
    private final String eventMetadataPayload;

    public static Builder builder()
    {
        return new Builder();
    }

    private StoredEvent(
        long id,
        @Nonnull String streamId,
        @Nonnull OffsetDateTime occurredOn,
        @Nonnull String eventTypeName,
        @Nonnull String eventPayload,
        @Nonnull String eventMetadataPayload)
    {
        this.id = id;
        this.streamId =streamId;
        this.occurredOn = occurredOn;
        this.eventTypeName = eventTypeName;
        this.eventPayload =eventPayload;
        this.eventMetadataPayload = eventMetadataPayload;
    }

    public long id()
    {
        return this.id;
    }

    @Nonnull
    public String streamId()
    {
        return this.streamId;
    }

    @Nonnull
    public OffsetDateTime occurredOn()
    {
        return this.occurredOn;
    }

    @Nonnull
    public Class<? extends DomainEvent> eventType()
    {
        return EventTypeRegistry.instance().typeOfName(this.eventTypeName);
    }

    @Nonnull
    public String eventTypeName()
    {
        return this.eventTypeName;
    }

    @Nonnull
    public String eventPayload()
    {
        return this.eventPayload;
    }

    @Nonnull
    public EventMetadata eventMetadata()
    {
        return ObjectSerializer.instance().deserialize(this.eventMetadataPayload, EventMetadata.class);
    }

    public static final class Builder
    {

        private Long id;

        private String streamId;

        private OffsetDateTime occurredOn;

        private String eventTypeName;

        private String eventPayload;

        private String eventMetadataPayload;

        private Builder()
        {
            // nop
        }

        public Builder withId(@Nonnull Long id)
        {
            this.id = id;
            return this;
        }

        public Builder withStreamId(@Nonnull String streamId)
        {
            this.streamId = streamId;
            return this;
        }

        public Builder occurredOn(@Nonnull OffsetDateTime occurredOn)
        {
            this.occurredOn = occurredOn;
            return this;
        }

        public Builder withEventTypeName(@Nonnull String eventTypeName)
        {
            this.eventTypeName = eventTypeName;
            return this;
        }

        public Builder withEventPayload(@Nonnull String eventPayload)
        {
            this.eventPayload = eventPayload;
            return this;
        }

        public Builder withEventMetadataPayload(@Nonnull String eventMetadataPayload)
        {
            this.eventMetadataPayload = eventMetadataPayload;
            return this;
        }

        public StoredEvent build()
        {
            return new StoredEvent(
                this.id,
                this.streamId,
                this.occurredOn,
                this.eventTypeName,
                this.eventPayload,
                this.eventMetadataPayload
            );
        }
    }
}
