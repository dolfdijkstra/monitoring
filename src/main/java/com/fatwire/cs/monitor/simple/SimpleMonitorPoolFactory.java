package com.fatwire.cs.monitor.simple;

import com.fatwire.cs.monitor.MonitorPool;
import com.fatwire.cs.monitor.MonitorPoolFactory;

public class SimpleMonitorPoolFactory extends MonitorPoolFactory {
	private MonitorPool pool;


	protected MonitorPool getMonitorPool() {
		if (pool == null) {
			pool = new SimpleMonitorPool();
		}
		return pool;
	}
}
