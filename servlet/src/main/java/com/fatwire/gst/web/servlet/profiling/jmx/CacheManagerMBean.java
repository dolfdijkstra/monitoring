package com.fatwire.gst.web.servlet.profiling.jmx;

public interface CacheManagerMBean {

    void start() throws Exception;

    void shutdown() throws Exception;
    
    int getNumberOfCaches();

}
