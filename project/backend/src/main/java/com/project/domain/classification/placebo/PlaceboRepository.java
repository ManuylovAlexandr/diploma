package com.project.domain.classification.placebo;

import static com.project.common.CollectionUtils.mapToSet;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import javax.annotation.Nonnull;

import com.project.domain.classification.therapyclass.ArtifactRepository;

public interface PlaceboRepository extends ArtifactRepository<Placebo, Placebo.Id>
{

    @Nonnull
    @Override
    default Class<Placebo> artifactType()
    {
        return Placebo.class;
    }

    @Nonnull
    @Override
    default Optional<Placebo> ofStringId(@Nonnull String identity)
    {
        return ofId(new Placebo.Id(identity));
    }

    @Nonnull
    @Override
    default Collection<Placebo> ofStringIds(@Nonnull Set<String> identities)
    {
        return ofIds(mapToSet(identities, Placebo.Id::new));
    }
}
