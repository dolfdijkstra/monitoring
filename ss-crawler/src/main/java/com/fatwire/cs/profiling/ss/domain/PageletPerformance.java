package com.fatwire.cs.profiling.ss.domain;

public class PageletPerformance {
    
    private long id;
    
    private CrawlSession session;
    
    private long downloadTime;

    /**
     * @return the downloadTime
     */
    public long getDownloadTime() {
        return downloadTime;
    }

    /**
     * @param downloadTime the downloadTime to set
     */
    public void setDownloadTime(long downloadTime) {
        this.downloadTime = downloadTime;
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
     * @return the session
     */
    public CrawlSession getSession() {
        return session;
    }

    /**
     * @param session the session to set
     */
    public void setSession(CrawlSession session) {
        this.session = session;
    }

}
