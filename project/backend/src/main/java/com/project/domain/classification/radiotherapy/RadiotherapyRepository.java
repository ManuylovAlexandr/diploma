package com.project.domain.classification.radiotherapy;

import static com.project.common.CollectionUtils.mapToSet;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import javax.annotation.Nonnull;

import com.project.domain.classification.therapyclass.ArtifactRepository;

public interface RadiotherapyRepository extends ArtifactRepository<Radiotherapy, Radiotherapy.Id>
{

    @Nonnull
    @Override
    default Class<Radiotherapy> artifactType()
    {
        return Radiotherapy.class;
    }

    @Nonnull
    @Override
    default Optional<Radiotherapy> ofStringId(@Nonnull String identity)
    {
        return ofId(new Radiotherapy.Id(identity));
    }

    @Nonnull
    @Override
    default Collection<Radiotherapy> ofStringIds(@Nonnull Set<String> identities)
    {
        return ofIds(mapToSet(identities, Radiotherapy.Id::new));
    }
}
