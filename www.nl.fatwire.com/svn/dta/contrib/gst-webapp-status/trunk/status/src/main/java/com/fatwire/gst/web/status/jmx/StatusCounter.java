package com.fatwire.gst.web.status.jmx;

import com.fatwire.gst.web.status.RequestCounter;

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
