package org.isel.jingle.util.iterators;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

public class IteratorTakeWhile<T> implements Iterator<T> {
    private final Iterator<T> src;
    private T elem = null;
    private final Predicate<T> pred;

    public IteratorTakeWhile(Iterable<T> src, Predicate<T> pred) {
        this.src = src.iterator();
        this.pred = pred;
    }

    @Override
    public boolean hasNext() {
        if(pred.test(elem)) {
            return false;
        }
        elem = src.next();
        return true;
    }

    @Override
    public T next() {
        if (!hasNext()) throw new NoSuchElementException();
        T aux = elem;
        elem = null;
        return aux;
    }
}
