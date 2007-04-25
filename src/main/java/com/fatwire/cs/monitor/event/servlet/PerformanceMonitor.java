package com.fatwire.cs.monitor.event.servlet;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.fatwire.cs.monitor.Statistics;
import com.fatwire.cs.monitor.event.FetchEvent;
import com.fatwire.cs.monitor.event.FetchEventListener;
import com.fatwire.cs.monitor.simple.SimpleStatistics;

public class PerformanceMonitor extends SimpleStatistics implements
        FetchEventListener, PerformanceMonitorMBean {

    public PerformanceMonitor(final String name) {
        super(name);
    }

    private final Map<String, SimpleStatistics> childNameHash = new TreeMap<String, SimpleStatistics>();

    public void fetchPerformed(final FetchEvent event) {
        update(event);
        updateForChildren(event);
    }

    private void update(final FetchEvent event) {
        super.addValue(event.getEndTime() - event.getStartTime());

    }

    protected void updateForChildren(final FetchEvent event) {
        //        final Object s = event.getSource();
        //
        //        if (s instanceof PieceMetaData) {
        //            final PieceMetaData pmd = (PieceMetaData) s;
        //            final String pagename = pmd.getPagename();
        //            if (pagename != null) {
        //                getInternalStatForPagename(pagename).update(event);
        //            }
        //        }

    }

    public Statistics getChildStatistics(final String name) {
        return childNameHash.get(name);

    }

    public Set getChildNames() {
        return Collections.unmodifiableSet(childNameHash.keySet());
    }

    protected SimpleStatistics getInternalStatForChild(final String name) {
        SimpleStatistics impl = childNameHash.get(name);
        if (impl == null) {
            impl = new SimpleStatistics(name);
            childNameHash.put(name, impl);
        }
        return impl;
    }

    public void reset() {
        super.reset();
        childNameHash.clear();

    }

}
