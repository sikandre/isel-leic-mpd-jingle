package org.isel.jingle.util;

import java.util.ArrayList;
import java.util.Spliterator;
import java.util.function.Consumer;

import static java.util.Spliterators.AbstractSpliterator;

public class SpliteratorCache<T> extends AbstractSpliterator {
    private final Spliterator<T> src;
    private final ArrayList<T> list;
    private int idx;

    public SpliteratorCache(Spliterator<T> src, ArrayList<T> list) {
        super(Long.MAX_VALUE, Spliterator.ORDERED);
        this.src = src;
        this.list = list;
        idx = 0;
    }

    @Override
    public boolean tryAdvance(Consumer action) {
        if(idx<list.size()){
            action.accept(list.get(idx++));
            return true;
        }
        boolean advanded = src.tryAdvance(t -> list.add(idx, t));
        if (advanded) {
            action.accept(list.get(idx++));
        }
        return advanded;
    }
}
