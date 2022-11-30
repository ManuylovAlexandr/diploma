package com.project.domain.classification;

import java.util.Collection;
import java.util.Optional;

import javax.annotation.Nonnull;

public interface EntityRepository<Entity, EntityId>
{

    Optional<Entity> ofId(@Nonnull EntityId id);

    Optional<Entity> ofStringId(@Nonnull String id);

    void save(@Nonnull Entity entity);

    Collection<Entity> all();
}
