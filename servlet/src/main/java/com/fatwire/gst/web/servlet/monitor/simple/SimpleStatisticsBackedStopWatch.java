package com.fatwire.gst.web.servlet.monitor.simple;

import com.fatwire.gst.web.servlet.monitor.StopWatch;

public class SimpleStatisticsBackedStopWatch extends SimpleStopWatch implements
		StopWatch {
	final SimpleStatistics stats;

	public SimpleStatisticsBackedStopWatch(final SimpleStatistics stats) {
		super();
		this.stats = stats;
	}

	public void stop() {
		super.stop();
		stats.addValue(getTimeElapsedInMillis());
	}
}
