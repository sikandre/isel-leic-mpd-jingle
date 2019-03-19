package org.isel.jingle.util.iterators;

import java.util.Iterator;
import java.util.function.Function;

public class IteratorFlatMap <T, R> implements Iterator<R> {

    private Iterator <R> subSrc = null;
    final Iterator <T> src;
    final Function<T, Iterable<R>> mapper;
    private R current;

    public IteratorFlatMap(Iterable<T> src, Function<T, Iterable<R>> mapper){
        this.src = src.iterator();
        this.mapper = mapper;

    }
    @Override
    public boolean hasNext() {
        if (subSrc != null && src.hasNext()){
            current = subSrc.next();
            return true;
        }
        if(src.hasNext()){
            subSrc = (Iterator<R>) src.next();
            return true;
        }
        return false;
    }

    @Override
    public  R next() {
        R aux = current;
        current = null;
        return aux;
    }
}
