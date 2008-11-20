package com.fatwire.cs.profiling.concurrent;

import java.util.Collection;

public interface ConcurrencyCounter<T,E> {

    void start(T t);
    
    void end(T t);

    long getTotalCount();
    
    int getConcurrencyCount();
    
    Collection<E> getCurrentExecutingOperations();
    
    String getName();
    
    void reset();
}
