package org.isel.jingle.util;

import org.isel.jingle.util.Spliterator.SpliteratorCache;
import org.isel.jingle.util.Spliterator.SpliteratorMerge;

import java.util.ArrayList;
import java.util.Spliterator;
import java.util.function.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class StreamUtils {

    public static <T> Supplier<Stream<T>> cache(Stream<T> src){
        Spliterator<T> split = src.spliterator();
        ArrayList<T> list = new ArrayList<>();
        return () -> StreamSupport.stream(new SpliteratorCache<>(split,list), false);
    }

    public static <T, U, R> Supplier<Stream<R>> merge(Stream<T> str,
                                                      Stream<U> nrs,
                                                      BiPredicate<T, U> predicate,
                                                      BiFunction<T, U, R> combine,
                                                      U def){
        return () -> StreamSupport.stream(new SpliteratorMerge<>(str, nrs, predicate, combine, def), false);
    }

}
