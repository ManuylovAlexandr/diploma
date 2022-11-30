package com.project.domain.classification.drug;

import static com.project.common.CollectionUtils.mapToSet;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import javax.annotation.Nonnull;

import com.project.domain.classification.therapyclass.ArtifactRepository;

public interface DrugRepository extends ArtifactRepository<Drug, Drug.Id>
{

    @Nonnull
    @Override
    default Class<Drug> artifactType()
    {
        return Drug.class;
    }

    @Nonnull
    @Override
    default Optional<Drug> ofStringId(@Nonnull String identity)
    {
        return ofId(new Drug.Id(identity));
    }

    @Nonnull
    @Override
    default Collection<Drug> ofStringIds(@Nonnull Set<String> identities)
    {
        return ofIds(mapToSet(identities, Drug.Id::new));
    }
}
