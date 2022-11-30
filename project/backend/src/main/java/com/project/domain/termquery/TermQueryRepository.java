package com.project.domain.termquery;

import java.util.Optional;
import java.util.Set;

import javax.annotation.Nonnull;

import com.project.domain.classification.EntityRepository;

public interface TermQueryRepository extends EntityRepository<TermQuery, TermQuery.Id>
{

    @Nonnull
    Set<TermQuery> all();

    @Nonnull
    @Override
    default Optional<TermQuery> ofStringId(@Nonnull String identity)
    {
        return ofId(TermQuery.Id.of(identity));
    }
}
