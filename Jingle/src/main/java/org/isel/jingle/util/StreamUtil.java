package org.isel.jingle.util;

import java.util.ArrayList;
import java.util.Spliterator;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class StreamUtil<T> {

    public static <T> Supplier<Stream<T>> cache(Stream<T> src){
        Spliterator<T> split = src.spliterator();
        ArrayList<T> list = new ArrayList<>();
        return () -> StreamSupport.stream(new SpliteratorCache<>(split,list), false);
    }

}
