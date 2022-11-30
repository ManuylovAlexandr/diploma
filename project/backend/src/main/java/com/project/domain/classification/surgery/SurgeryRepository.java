package com.project.domain.classification.surgery;

import static com.project.common.CollectionUtils.mapToSet;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import javax.annotation.Nonnull;

import com.project.domain.classification.therapyclass.ArtifactRepository;

public interface SurgeryRepository extends ArtifactRepository<Surgery, Surgery.Id>
{

    @Nonnull
    @Override
    default Class<Surgery> artifactType()
    {
        return Surgery.class;
    }

    @Nonnull
    @Override
    default Optional<Surgery> ofStringId(@Nonnull String identity)
    {
        return ofId(new Surgery.Id(identity));
    }

    @Nonnull
    @Override
    default Collection<Surgery> ofStringIds(@Nonnull Set<String> identities)
    {
        return ofIds(mapToSet(identities, Surgery.Id::new));
    }
}
