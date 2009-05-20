package com.fatwire.cs.profiling.concurrent.jmx;


public interface StatusCounterMBean {

    public int getConcurrencyCount();

    public long getTotalCount();

}
