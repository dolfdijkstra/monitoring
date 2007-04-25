package com.fatwire.cs.monitor;

public class SW {

	private SW() {
	}

	public static StopWatch getStopWatch(final String name) {
		return MonitorPoolFactory.getFactory().getMonitorPool().getStopWatch(
				name);
	}

}
