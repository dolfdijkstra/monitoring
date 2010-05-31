package com.fatwire.gst.web.servlet.monitor.simple;

import com.fatwire.gst.web.servlet.monitor.MonitorPool;
import com.fatwire.gst.web.servlet.monitor.MonitorPoolFactory;

public class SimpleMonitorPoolFactory extends MonitorPoolFactory {
	private MonitorPool pool;


	protected MonitorPool getMonitorPool() {
		if (pool == null) {
			pool = new SimpleMonitorPool();
		}
		return pool;
	}
}
