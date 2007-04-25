package com.fatwire.cs.profiling.jmx;

import java.io.IOException;
import java.util.Date;

public interface CacheStatsMBean {

    /**
     * @return
     * @see com.fatwire.cs.core.cache.RuntimeCacheStats#getClearCount()
     */
    public long getClearCount() throws IOException;

    /**
     * @return
     * @see com.fatwire.cs.core.cache.RuntimeCacheStats#getCreatedDate()
     */
    public Date getCreatedDate() throws IOException;

    /**
     * @return
     * @see com.fatwire.cs.core.cache.RuntimeCacheStats#getHits()
     */
    public long getHits() throws IOException;

    /**
     * @return
     * @see com.fatwire.cs.core.cache.RuntimeCacheStats#getLastFlushedDate()
     */
    public Date getLastFlushedDate() throws IOException;

    /**
     * @return
     * @see com.fatwire.cs.core.cache.RuntimeCacheStats#getLastPrunedDate()
     */
    public Date getLastPrunedDate() throws IOException;

    /**
     * @return
     * @see com.fatwire.cs.core.cache.RuntimeCacheStats#getMisses()
     */
    public long getMisses() throws IOException;

    /**
     * @return
     * @see com.fatwire.cs.core.cache.RuntimeCacheStats#getRemoveCount()
     */
    public long getRemoveCount() throws IOException;

    /**
     * @return
     * @see com.fatwire.cs.core.cache.RuntimeCacheStats#hasINotifyObjects()
     */
    public boolean hasINotifyObjects() throws IOException;

}