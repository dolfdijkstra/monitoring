package com.fatwire.cs.profiling.jmx;

public interface CacheManagerMBean {

    void setDomainName(String domainName);

    void start() throws Exception;

    void shutdown() throws Exception;

}
