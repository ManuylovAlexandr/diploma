package com.project.common;

import javax.annotation.Nonnull;

import com.project.domain.event.EventMetadata;

public final class EventMetadataHolder
{

    private static final ThreadLocal<EventMetadata.Builder> BUILDER = ThreadLocal.withInitial(EventMetadata::builder);

    public static void setAuthorName(@Nonnull String authorName)
    {
        BUILDER.set(BUILDER.get().withAuthorName(authorName));
    }

    @Nonnull
    public static EventMetadata.Builder builder()
    {
        return EventMetadata.Builder.copy(BUILDER.get());
    }

    public static void reset()
    {
        BUILDER.remove();
    }

    private EventMetadataHolder()
    {
        // nop
    }
}
