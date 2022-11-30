package com.project.common;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import javax.annotation.Nonnull;

public final class CollectionUtils
{

    @Nonnull
    public static <T, R> Set<R> map(@Nonnull Set<T> set, @Nonnull Function<? super T, R> mapper)
    {
        return set.stream().map(mapper).collect(toSet());
    }

    @Nonnull
    public static <T, R> Set<R> flatMap(@Nonnull Collection<T> set, @Nonnull Function<? super T, Set<R>> mapper)
    {
        return set.stream()
            .flatMap(item -> mapper.apply(item).stream())
            .collect(toSet());
    }

    public static <T, R> List<R> mapToList(@Nonnull Collection<T> collection, @Nonnull Function<? super T, R> mapper)
    {
        return collection.stream().map(mapper).collect(toList());
    }

    @Nonnull
    public static <T, R> Set<R> mapToSet(@Nonnull Collection<T> collection, @Nonnull Function<? super T, R> mapper)
    {
        return collection.stream().map(mapper).collect(toSet());
    }

    @Nonnull
    public static <T, R> Collection<R> map(@Nonnull Collection<T> collection, @Nonnull Function<? super T, R> mapper)
    {
        return collection.stream().map(mapper).collect(toList());
    }

    @Nonnull
    public static <T, R> List<R> map(@Nonnull List<T> list, @Nonnull Function<? super T, R> mapper)
    {
        return list.stream().map(mapper).collect(toList());
    }

    @Nonnull
    public static <T, R> List<R> map(@Nonnull T[] array, @Nonnull Function<? super T, R> mapper)
    {
        return stream(array).map(mapper).collect(toList());
    }

    @Nonnull
    public static <K1, K2, V1, V2> Map<K2, V2> map(
        @Nonnull Map<K1, V1> map,
        @Nonnull Function<? super K1, K2> keyMapper,
        @Nonnull Function<? super V1, V2> valueMapper
    )
    {

        return map.entrySet().stream().collect(toMap(
            entry -> keyMapper.apply(entry.getKey()),
            entry -> valueMapper.apply(entry.getValue())
        ));
    }

    @Nonnull
    public static <T, K, V> Map<K, V> map(
        @Nonnull Collection<T> collection,
        @Nonnull Function<T, K> keyMapper,
        @Nonnull Function<T, V> valueMapper
    )
    {

        return collection.stream().collect(toMap(keyMapper, valueMapper));
    }

    public static <T> boolean isNotEmpty(Collection<T> collection)
    {
        return collection != null && !collection.isEmpty();
    }

    private CollectionUtils()
    {
    }
}

