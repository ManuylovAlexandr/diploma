package com.project.domain.classification;


import javax.annotation.Nonnull;

public abstract class CaseInsensitiveName
{

    @Nonnull
    private final String name;

    protected CaseInsensitiveName(@Nonnull String name)
    {
        this.name = name;
    }

    @Nonnull
    public String asText()
    {
        return this.name;
    }

    @Override
    public int hashCode()
    {
        return this.name.toLowerCase().hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof CaseInsensitiveName that))
        {
            return false;
        }
        return this.name.equalsIgnoreCase(that.name);
    }
}
