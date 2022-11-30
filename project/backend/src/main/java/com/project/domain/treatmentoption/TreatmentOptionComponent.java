package com.project.domain.treatmentoption;

import java.util.Objects;
import java.util.Set;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public interface TreatmentOptionComponent
{

    @Nonnull
    String identity();


    @Nonnull
    Class<? extends TreatmentOptionComponent> entityType();

    @Nonnull
    Set<TreatmentOptionComponent.Id> components();

    default boolean isComposite()
    {
        return !components().isEmpty();
    }

    default boolean hasSameIdentityAs(TreatmentOptionComponent other)
    {
        var thisId = new Id(identity(), entityType());
        var otherId = new Id(other.identity(), entityType());
        return thisId.equals(otherId);
    }

    default int hashCodeByIdentity()
    {
        var thisId = new Id(identity(), entityType());
        return thisId.hashCode();
    }

    final class Id
    {

        @Nonnull
        private final String identity;

        @Nonnull
        private final String type;

        public Id(@Nonnull String identity, @Nonnull Class<? extends TreatmentOptionComponent> type)
        {
            this.identity = identity;
            this.type = type.getSimpleName();
        }

        public Id(@Nonnull String identity, @Nonnull String type)
        {
            this.identity = identity;
            this.type = type;
        }

        @Nonnull
        public String identity()
        {
            return this.identity;
        }

        @Nonnull
        public String type()
        {
            return this.type;
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o)
            {
                return true;
            }
            if (o == null || getClass() != o.getClass())
            {
                return false;
            }
            Id id1 = (Id) o;
            return this.identity.equals(id1.identity) && this.type.equals(id1.type);
        }

        @Override
        public int hashCode()
        {
            return Objects.hash(this.identity, this.type);
        }

        @JsonCreator
        protected static Id fromJson(
            @JsonProperty("identity") String identity,
            @JsonProperty("type") String type)
        {
            return new Id(identity, type);
        }
    }
}
