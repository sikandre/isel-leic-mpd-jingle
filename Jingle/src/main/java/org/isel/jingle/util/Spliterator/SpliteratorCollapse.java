package org.isel.jingle.util.Spliterator;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators.AbstractSpliterator;
import java.util.function.Consumer;

public class SpliteratorCollapse<T> extends AbstractSpliterator<T> {
    private final Iterator<T> iter;
    private T current = null;
    private T last = null;

    public SpliteratorCollapse(Iterable<T> src) {
        super(Long.MAX_VALUE, Spliterator.ORDERED);
        iter = src.iterator();
    }

    @Override
    public boolean tryAdvance(Consumer<? super T> action) {
        while (iter.hasNext()) {
            current = iter.next();
            if (current != last) {
                last = current;
                action.accept(last);
                return true;
            }
        }
        return false;
    }
}
