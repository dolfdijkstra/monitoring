package com.fatwire.gst.web.servlet.profiling.jmx;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Date;

import COM.FutureTense.Util.ftTimedHashtable;

import com.fatwire.cs.core.cache.RuntimeCacheStats;

public class CacheStats implements CacheStatsMBean {
    private final String hashName;

    private WeakReference<ftTimedHashtable> hashRef;

    /**
     * @param delegate
     */
    public CacheStats(final String hashName) {
        super();
        this.hashName = hashName;
    }

    /* (non-Javadoc)
     * @see com.fatwire.gst.web.servlet.profiling.jmx.RuntimeCacheStatsMBean#getClearCount()
     */
    public long getClearCount() throws IOException {
        return getDelegate().getClearCount();
    }

    /* (non-Javadoc)
     * @see com.fatwire.gst.web.servlet.profiling.jmx.RuntimeCacheStatsMBean#getCreatedDate()
     */
    public Date getCreatedDate() throws IOException {
        return getDelegate().getCreatedDate();
    }

    /* (non-Javadoc)
     * @see com.fatwire.gst.web.servlet.profiling.jmx.RuntimeCacheStatsMBean#getHits()
     */
    public long getHits() throws IOException {
        return getDelegate().getHits();
    }

    public long getSize() throws IOException {
        getDelegate();
        ftTimedHashtable t = hashRef.get();
        if (t != null)
            return t.size();
        return 0;
    }

    /* (non-Javadoc)
     * @see com.fatwire.gst.web.servlet.profiling.jmx.RuntimeCacheStatsMBean#getLastFlushedDate()
     */
    public long getLastFlushedElapsed() throws IOException {
        return getElapsed(getDelegate().getLastFlushedDate());
    }

    /* (non-Javadoc)
     * @see com.fatwire.gst.web.servlet.profiling.jmx.RuntimeCacheStatsMBean#getLastPrunedDate()
     */
    public long getLastPrunedElapsed() throws IOException {
        return getElapsed(getDelegate().getLastPrunedDate());
    }

    /**
     * 
     * 
     * @param then
     * @return the time between then and now in seconds
     */
    private long getElapsed(Date then) {
        return then != null ? (System.currentTimeMillis() - then.getTime()) / 1000
                : 0;

    }

    /* (non-Javadoc)
     * @see com.fatwire.gst.web.servlet.profiling.jmx.RuntimeCacheStatsMBean#getMisses()
     */
    public long getMisses() throws IOException {
        return getDelegate().getMisses();
    }

    /* (non-Javadoc)
     * @see com.fatwire.gst.web.servlet.profiling.jmx.RuntimeCacheStatsMBean#getRemoveCount()
     */
    public long getRemoveCount() throws IOException {
        return getDelegate().getRemoveCount();
    }

    /* (non-Javadoc)
     * @see com.fatwire.gst.web.servlet.profiling.jmx.RuntimeCacheStatsMBean#hasINotifyObjects()
     */
    public boolean hasINotifyObjects() throws IOException {
        return getDelegate().hasINotifyObjects();
    }

    private RuntimeCacheStats getDelegate() {
        //TODO figure out how long this can be cached.
        ftTimedHashtable ftTh = (hashRef == null) ? null : hashRef.get();
        if (ftTh == null) {
            ftTh = ftTimedHashtable.findHash(hashName);
            if (ftTh != null) {
                hashRef = new WeakReference<ftTimedHashtable>(ftTh);
            }
        }

        if (ftTh == null) {
            return new NullRuntimeCacheStats();
        }

        return ftTh.getRuntimeStats();
    }

}
