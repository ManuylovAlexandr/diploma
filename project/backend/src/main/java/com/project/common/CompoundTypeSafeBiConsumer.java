package com.project.common;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import javax.annotation.Nonnull;

public class CompoundTypeSafeBiConsumer<T, U> implements BiConsumer<T, U>
{

    @Nonnull
    private final Map<Class<? extends T>, BiConsumer<? super T, U>> consumersPerType;

    @Nonnull
    public static <T, U> CompoundTypeSafeBiConsumer.Builder<T, U> builder()
    {
        return new CompoundTypeSafeBiConsumer.Builder<>();
    }

    private CompoundTypeSafeBiConsumer(@Nonnull Map<Class<? extends T>, BiConsumer<? super T, U>> consumersPerType)
    {
        this.consumersPerType = consumersPerType;
    }

    @Override
    public void accept(@Nonnull T value, @Nonnull U secondArgument)
    {
        var valueType = value.getClass();
        var consumer = this.consumersPerType.get(valueType);
        consumer.accept(value, secondArgument);
    }

    public static final class Builder<T, U>
    {

        private final Map<Class<? extends T>, BiConsumer<? super T, U>> consumersPerType;

        private Builder()
        {
            this.consumersPerType = new HashMap<>();
        }

        @Nonnull
        public <E extends T> CompoundTypeSafeBiConsumer.Builder<T, U> withConsumer(
            @Nonnull Class<E> type, @Nonnull
            BiConsumer<? super E, U> consumer)
        {
            addConsumer(type, consumer);
            return this;
        }

        @Nonnull
        public CompoundTypeSafeBiConsumer<T, U> build()
        {
            return new CompoundTypeSafeBiConsumer<>(this.consumersPerType);
        }

        private <E extends T> void addConsumer(Class<E> type, BiConsumer<? super E, U> consumer)
        {
            this.consumersPerType.put(type, (BiConsumer<? super T, U>) consumer);
        }
    }
}
