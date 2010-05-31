package com.fatwire.cs.profiling.concurrent.jmx;

import com.fatwire.cs.profiling.concurrent.RequestCounter;

public class StatusCounter implements StatusCounterMBean {
    private final RequestCounter delegate;

    public StatusCounter(RequestCounter requestCounter) {
        this.delegate = requestCounter;
    }

    public int getConcurrencyCount() {

        return delegate.getConcurrencyCount();
    }


    public long getTotalCount() {
        return delegate.getTotalCount();
    }

}
