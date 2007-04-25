package com.fatwire.cs.monitor.simple;

import com.fatwire.cs.monitor.StopWatch;

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
