package com.project.domain.event;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class EventMetadata
{

    @Nonnull
    private final String authorName;

    @Nonnull
    public static Builder builder()
    {
        return new Builder();
    }

    @JsonCreator
    private EventMetadata(@JsonProperty("authorName") @Nonnull String authorName)
    {
        this.authorName = authorName;
    }

    @Nonnull
    public String authorName()
    {
        return this.authorName;
    }

    public static final class Builder
    {

        private String authorName;

        @Nonnull
        public static Builder copy(Builder original)
        {
            return new Builder(original);
        }

        private Builder()
        {

        }

        private Builder(Builder original)
        {
            this.authorName = original.authorName;
        }

        @Nonnull
        public Builder withAuthorName(@Nonnull String authorName)
        {
            this.authorName = authorName;
            return this;
        }

        @Nonnull
        public EventMetadata build()
        {
            return new EventMetadata(this.authorName);
        }
    }
}
