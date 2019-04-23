package org.isel.jingle.util.Spliterator;

import org.isel.jingle.util.StreamUtils;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.*;
import java.util.stream.Stream;

public class SpliteratorMerge<T, U, R> extends Spliterators.AbstractSpliterator<R> {
    private final Spliterator<T> str;
    private final BiPredicate<T, U> predicate;
    private  Supplier<Stream<U>> cache;
    private final BiFunction<T, U, R> combine;
    private final U def;

    public SpliteratorMerge (Stream<T> src, Stream<U> nrs, BiPredicate<T, U> predicate, BiFunction<T, U, R> comb, U def) {
        super(Long.MAX_VALUE, Spliterator.ORDERED);
        str = src.spliterator();
        cache = StreamUtils.cache(nrs);
        this.predicate = predicate;
        this.combine = comb;
        this.def = def;
    }

    @Override
    public boolean tryAdvance(Consumer<? super R> action) {
        return str.tryAdvance(s -> action.accept(combine.apply(s, cache.get()
                .filter(n -> predicate.test(s, n))
                .findFirst()
                .orElse(def))));
    }
}
