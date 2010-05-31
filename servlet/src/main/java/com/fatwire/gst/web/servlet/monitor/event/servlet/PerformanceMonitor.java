package com.fatwire.gst.web.servlet.monitor.event.servlet;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.fatwire.gst.web.servlet.monitor.Statistics;
import com.fatwire.gst.web.servlet.monitor.commons.math.SummaryStatisticsAdapter;
import com.fatwire.gst.web.servlet.monitor.event.FetchEvent;
import com.fatwire.gst.web.servlet.monitor.event.FetchEventListener;

public class PerformanceMonitor extends SummaryStatisticsAdapter implements
        FetchEventListener, PerformanceMonitorMBean {

    public PerformanceMonitor(final String name) {
        super(name);
    }

    private final Map<String, SummaryStatisticsAdapter> childNameHash = new TreeMap<String, SummaryStatisticsAdapter>();

    public void fetchPerformed(final FetchEvent event) {
        update(event);
        updateForChildren(event);
    }

    private void update(final FetchEvent event) {
        super.addValue(event.getEndTime() - event.getStartTime());

    }

    protected void updateForChildren(final FetchEvent event) {

    }

    public Statistics getChildStatistics(final String name) {
        return childNameHash.get(name);

    }

    public Set<String> getChildNames() {
        return Collections.unmodifiableSet(childNameHash.keySet());
    }

    protected SummaryStatisticsAdapter getInternalStatForChild(final String name) {
        SummaryStatisticsAdapter impl = childNameHash.get(name);
        if (impl == null) {
            impl = new SummaryStatisticsAdapter(name);
            childNameHash.put(name, impl);
        }
        return impl;
    }

    public void reset() {
        super.reset();
        childNameHash.clear();

    }

}
