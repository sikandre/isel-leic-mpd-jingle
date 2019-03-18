package org.isel.jingle.util.iterators;

import java.util.Iterator;

public class IteratorFrom<T> implements Iterator<T> {
    private final T[] src;
    private int idx;
    private T current = null;

    public IteratorFrom(T[] src) {
        this.src = src;
        idx = 0;
    }

    @Override
    public boolean hasNext() {
        if(src.length > idx){
            current = src[idx++];
            return true;
        }
        return false;
    }

    @Override
    public T next() {
        T aux = current;
        current = null;
        return aux;
    }
}
