package com.fatwire.gst.web.servlet.monitor.event;

import java.util.EventObject;

public class FetchEvent extends EventObject {

    /**
     * 
     */
    private static final long serialVersionUID = 0L;

    private final long startTime;

    private final long endTime;
    
    private final String url;

    /**
     * @param source
     * @param startTime
     * @param endTime
     * @param url
     */
    public FetchEvent(final Object source, final long startTime, final long endTime, final String url) {
        super(source);
        this.startTime = startTime;
        this.endTime = endTime;
        this.url = url;
    }

    /**
     * @return the endTime
     */
    public long getEndTime() {
        return endTime;
    }

    /**
     * @return the startTime
     */
    public long getStartTime() {
        return startTime;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

}
