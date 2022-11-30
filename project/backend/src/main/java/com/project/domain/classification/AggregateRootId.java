package com.project.domain.classification;


import java.util.Objects;

import javax.annotation.Nonnull;


public abstract class AggregateRootId
{

    public static final int MAX_ID_LENGTH = 64;

    @Nonnull
    private final String id;

    public static boolean isAllowedCharacter(int aCodePoint)
    {
        if (!isAscii(aCodePoint))
        {
            return false;
        }
        char codePoint = ((char) aCodePoint);
        return Character.isLowerCase(codePoint)
            || Character.isDigit(codePoint)
            || codePoint == '_'
            || codePoint == '-';
    }

    protected AggregateRootId(@Nonnull String id)
    {
        this.id = id;
    }

    @Nonnull
    public String asText()
    {
        return this.id;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(this.id);
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
        AggregateRootId that = (AggregateRootId) o;
        return Objects.equals(this.id, that.id);
    }

    @Override
    public String toString()
    {
        return getClass().getSimpleName() + "(id='" + this.id + "')";
    }

    private static boolean isAscii(int codePoint)
    {
        return codePoint < 128;
    }
}
