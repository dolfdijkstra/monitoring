package com.fatwire.gst.web.servlet.monitor.simple;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;

import com.fatwire.gst.web.servlet.monitor.Monitor;
import com.fatwire.gst.web.servlet.monitor.MonitorPool;
import com.fatwire.gst.web.servlet.monitor.StopWatch;

public class SimpleMonitorPool implements MonitorPool {

    protected Map<String, SimpleMonitor> monitors = new HashMap<String, SimpleMonitor>();

    public Monitor getMonitor(final String name) {
        if (monitors.containsKey(name)) {
            return monitors.get(name);
        } else {
            final SimpleStatistics s = new SimpleStatistics(name);
            final SimpleMonitor mon = new SimpleMonitor(name, s);
            monitors.put(name, mon);
            return mon;
        }
    }

    public StopWatch getStopWatch(final String name) {
        final SimpleMonitor mon = (SimpleMonitor) getMonitor(name);
        return mon.createStopWatch();
    }

    public Iterator<Monitor> iterator() {
        final Iterator<String> itor = new TreeSet<String>(monitors.keySet())
                .iterator();

        return new Iterator<Monitor>() {

            public boolean hasNext() {
                return itor.hasNext();
            }

            public Monitor next() {
                final String key = itor.next();
                return monitors.get(key);
            }

            public void remove() {
                // todo silent or throw IllegalOperation()
            }

        };
    }

}
