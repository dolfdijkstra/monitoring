package com.fatwire.cs.profiling.jmx;

public interface CacheManagerMBean {

    void start() throws Exception;

    void shutdown() throws Exception;
    
    int getNumberOfCaches();

}
