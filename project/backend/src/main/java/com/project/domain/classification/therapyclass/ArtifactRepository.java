package com.project.domain.classification.therapyclass;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import javax.annotation.Nonnull;

import com.project.domain.classification.Artifact;
import com.project.domain.classification.AggregateRootId;
import com.project.domain.classification.EntityRepository;

public interface ArtifactRepository<E extends Artifact, I extends AggregateRootId> extends EntityRepository<E, I>
{

    @Nonnull
    Optional<E> ofId(@Nonnull I identity);

    @Nonnull
    Optional<E> ofStringId(@Nonnull String identity);

    @Nonnull
    Collection<E> ofIds(@Nonnull Set<I> identities);

    @Nonnull
    Collection<E> all();

    @Nonnull
    Class<E> artifactType();

    @Nonnull
    Collection<E> ofStringIds(@Nonnull Set<String> identities);
}
