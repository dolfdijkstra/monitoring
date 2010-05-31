package com.fatwire.gst.web.servlet.monitor.simple;

import com.fatwire.gst.web.servlet.monitor.Monitor;
import com.fatwire.gst.web.servlet.monitor.Statistics;

public class SimpleMonitor implements Monitor {

	private final String name;

	private final SimpleStatistics stats;

	private Monitor parent;

	public SimpleMonitor(final String name, final SimpleStatistics statistics) {
		super();
		this.name = name;
		stats = statistics;
	}

	public String getName() {
		return name;
	}

	public Monitor getParent() {
		return parent;
	}

	public Statistics getStatistics() {
		return stats;
	}

	public void setParent(final Monitor parent) {
		this.parent = parent;
	}

	protected SimpleStopWatch createStopWatch(){
		return new SimpleStatisticsBackedStopWatch(stats);
	}
	
}
