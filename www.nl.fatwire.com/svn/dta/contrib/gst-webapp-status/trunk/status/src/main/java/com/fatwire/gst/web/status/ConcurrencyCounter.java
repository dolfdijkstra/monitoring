package com.fatwire.gst.web.status;

import java.util.Collection;

public interface ConcurrencyCounter<T,E> {

    void start(T t);
    
    long end(T t);

    long getTotalCount();
    
    int getConcurrencyCount();
    
    Collection<E> getCurrentExecutingOperations();
    
    String getName();
    
    void reset();
}
