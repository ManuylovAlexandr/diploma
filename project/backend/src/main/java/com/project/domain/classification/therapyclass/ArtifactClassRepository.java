package com.project.domain.classification.therapyclass;

import java.util.Collection;
import java.util.Optional;

import javax.annotation.Nonnull;

import com.project.domain.classification.EntityRepository;

public interface ArtifactClassRepository<E extends ArtifactClassState & ArtifactClass>
    extends EntityRepository<E, ArtifactClass.Id>
{

    void save(@Nonnull E artifactClass);

    @Nonnull
    Optional<E> ofId(@Nonnull ArtifactClass.Id therapyClassId);

    @Nonnull
    default Optional<E> ofStringId(@Nonnull String id)
    {
        return ofId(new ArtifactClass.Id(id));
    }

    @Override
    Collection<E> all();
}
