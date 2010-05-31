package com.fatwire.gst.web.servlet.monitor.event.servlet;

import com.fatwire.gst.web.servlet.monitor.Statistics;

public interface PerformanceMonitorMBean extends Statistics {

    public void reset();
}
