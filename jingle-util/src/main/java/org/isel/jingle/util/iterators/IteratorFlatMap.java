package org.isel.jingle.util.iterators;

import java.util.Iterator;
<<<<<<< HEAD
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
=======
import java.util.NoSuchElementException;
import java.util.function.Function;

public class IteratorFlatMap<T, R> implements Iterator<R> {
    private final Iterator<T> src;
    private Iterator<R> subSrc = null;
    private final Function<T, Iterable<R>> mapper;
    private R current = null;

    public IteratorFlatMap(Iterable<T> src, Function<T, Iterable<R>> mapper) {
        this.mapper = mapper;
        this.src = src.iterator();
    }

    @Override
    public boolean hasNext() {
        if(subSrc!=null && subSrc.hasNext()){
>>>>>>> master
            current = subSrc.next();
            return true;
        }
        if(src.hasNext()){
<<<<<<< HEAD
            subSrc = (Iterator<R>) src.next();
            return true;
=======
            subSrc = mapper.apply(src.next()).iterator();
            if(subSrc.hasNext()) {
                current = subSrc.next();
                return true;
            }
            else {
                return true;
            }
>>>>>>> master
        }
        return false;
    }

    @Override
<<<<<<< HEAD
    public  R next() {
=======
    public R next() {
        if (current==null && !hasNext()) throw new NoSuchElementException();
>>>>>>> master
        R aux = current;
        current = null;
        return aux;
    }
}
