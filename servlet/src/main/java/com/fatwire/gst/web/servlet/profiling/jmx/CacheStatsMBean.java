package com.fatwire.gst.web.servlet.profiling.jmx;

import java.io.IOException;
import java.util.Date;

public interface CacheStatsMBean {

    /**
     * @return
     * @see com.fatwire.gst.web.servlet.core.cache.RuntimeCacheStats#getClearCount()
     */
    public long getClearCount() throws IOException;

    /**
     * @return
     * @see com.fatwire.gst.web.servlet.core.cache.RuntimeCacheStats#getCreatedDate()
     */
    public Date getCreatedDate() throws IOException;

    /**
     * @return
     * @see com.fatwire.gst.web.servlet.core.cache.RuntimeCacheStats#getHits()
     */
    public long getHits() throws IOException;

    /**
     * @return
     * @see com.fatwire.gst.web.servlet.core.cache.RuntimeCacheStats#getLastFlushedDate()
     */
    public long getLastFlushedElapsed() throws IOException;

    /**
     * @return
     * @see com.fatwire.gst.web.servlet.core.cache.RuntimeCacheStats#getLastPrunedDate()
     */
    public long getLastPrunedElapsed() throws IOException;

    /**
     * @return
     * @see com.fatwire.gst.web.servlet.core.cache.RuntimeCacheStats#getMisses()
     */
    public long getMisses() throws IOException;

    /**
     * @return
     * @see com.fatwire.gst.web.servlet.core.cache.RuntimeCacheStats#getRemoveCount()
     */
    public long getRemoveCount() throws IOException;

    /**
     * @return
     * @see com.fatwire.gst.web.servlet.core.cache.RuntimeCacheStats#hasINotifyObjects()
     */
    public boolean hasINotifyObjects() throws IOException;

    /**
     * The current size of the cache
     * @return
     * @throws IOException
     */
    public long getSize() throws IOException;
    
}