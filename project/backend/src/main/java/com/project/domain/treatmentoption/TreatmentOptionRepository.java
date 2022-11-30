package com.project.domain.treatmentoption;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import javax.annotation.Nonnull;

import com.project.domain.classification.EntityRepository;

public interface TreatmentOptionRepository extends EntityRepository<TreatmentOption, TreatmentOption.Id>
{

    @Nonnull
    @Override
    default Optional<TreatmentOption> ofStringId(@Nonnull String identity)
    {
        return ofId(TreatmentOption.Id.of(identity));
    }

    @Nonnull
    Set<TreatmentOption> all();

    @Nonnull
    Optional<? extends TreatmentOptionComponent> componentOfId(@Nonnull TreatmentOptionComponent.Id componentId);

    @Nonnull
    Collection<TreatmentOption> ofIds(@Nonnull Set<TreatmentOption.Id> ids);
}
