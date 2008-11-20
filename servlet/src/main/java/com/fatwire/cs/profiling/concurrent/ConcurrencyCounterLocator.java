package com.fatwire.cs.profiling.concurrent;

import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrencyCounterLocator {

    private static final ConcurrencyCounterLocator self = new ConcurrencyCounterLocator();

    private final Map<String, ConcurrencyCounter<?, ?>> map = new ConcurrentHashMap<String, ConcurrencyCounter<?, ?>>();

    private ConcurrencyCounterLocator() {
    }

    public ConcurrencyCounter<?, ?> locate(String name) {
        return map.get(name);
    }
    
    public Iterable<String> getNames(){
        return new HashSet<String>(map.keySet());
    }

    public void register(ConcurrencyCounter<?, ?> counter) {
        map.put(counter.getName(), counter);
    }

    public void deregister(ConcurrencyCounter<?, ?> counter) {
        map.remove(counter.getName());
    }

    public static final ConcurrencyCounterLocator getInstance() {
        return self;
    }
    

}
