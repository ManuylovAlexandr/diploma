package com.project.domain.classification;

import javax.annotation.Nonnull;

public final class EntityNotFoundException extends RuntimeException
{

    private static final long serialVersionUID = 1049398366200565345L;

    @Nonnull
    private final String entityType;

    @Nonnull
    private final String entityId;

    public EntityNotFoundException(
        @Nonnull String entityType,
        @Nonnull String entityId)
    {
        super();
        this.entityType = entityType;
        this.entityId = entityId;
    }

    @Nonnull
    public String entityType()
    {
        return this.entityType;
    }

    @Nonnull
    public String entityId()
    {
        return this.entityId;
    }

    @Override
    public String getMessage()
    {
        return this.entityType + " by provided id: " + this.entityId + " is not found.";
    }
}
