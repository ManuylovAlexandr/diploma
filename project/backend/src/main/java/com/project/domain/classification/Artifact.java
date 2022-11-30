package com.project.domain.classification;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import javax.annotation.Nonnull;

import com.project.domain.classification.therapyclass.ArtifactClass;
import com.project.domain.treatmentoption.TreatmentOptionComponent;

public interface Artifact extends TreatmentOptionComponent
{

    @Nonnull
    String identity();

    @Nonnull
    CaseInsensitiveName canonicalName();

    @Nonnull
    Optional<ArtifactClass.Id> artifactClass();

    @Nonnull
    @Override
    default Set<TreatmentOptionComponent.Id> components()
    {
        return Collections.emptySet();
    }

    @Nonnull
    default String canonicalNameAsText()
    {
        return canonicalName().asText();
    }

     Set<? extends CaseInsensitiveName> names();
}
