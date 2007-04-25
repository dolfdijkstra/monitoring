package com.fatwire.cs.monitor.event.servlet;

import com.fatwire.cs.monitor.Statistics;

public interface PerformanceMonitorMBean extends Statistics {

    public void reset();
}
