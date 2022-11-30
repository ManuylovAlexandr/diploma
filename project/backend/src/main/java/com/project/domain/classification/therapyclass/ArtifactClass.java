package com.project.domain.classification.therapyclass;

import javax.annotation.Nonnull;

import com.project.domain.classification.AggregateRootId;
import com.project.domain.classification.CaseInsensitiveName;


public interface ArtifactClass
{
    @Nonnull
    Id identity();

    class Id extends AggregateRootId
    {

        public Id(@Nonnull String id)
        {
            super(id);
        }
    }

    class Name extends CaseInsensitiveName
    {

        public Name(@Nonnull String id)
        {
            super(id);
        }
    }
}
