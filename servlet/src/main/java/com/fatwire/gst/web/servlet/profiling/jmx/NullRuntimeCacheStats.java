package com.fatwire.gst.web.servlet.profiling.jmx;

import java.util.Date;

import com.fatwire.cs.core.cache.RuntimeCacheStats;

public class NullRuntimeCacheStats implements RuntimeCacheStats {

    public long getClearCount() {
        return 0;
    }

    public Date getCreatedDate() {
        return new Date(0);
    }

    public long getHits() {
        return 0;
    }

    public Date getLastFlushedDate() {
        return new Date(0);
    }

    public Date getLastPrunedDate() {
        return new Date(0);    }

    public long getMisses() {
        return 0;
    }

    public long getRemoveCount() {
        return 0;
    }

    public boolean hasINotifyObjects() {
        return false;
    }

}
