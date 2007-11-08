package com.fatwire.cs.profiling.ss.domain;

import java.util.Date;

public class CrawlSession {

    private long id;

    private long hostConfigId;

    private Date startTime;

    private Date endTime;

    /**
     * @return the endTime
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * @param endTime the endTime to set
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * @return the hostConfigId
     */
    public long getHostConfigId() {
        return hostConfigId;
    }

    /**
     * @param hostConfigId the hostConfigId to set
     */
    public void setHostConfigId(long hostConfigId) {
        this.hostConfigId = hostConfigId;
    }

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return the startTime
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * @param startTime the startTime to set
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

}
