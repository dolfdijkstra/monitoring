package com.fatwire.cs.monitor.simple;

import com.fatwire.cs.monitor.StopWatch;

public class SimpleStopWatch implements StopWatch {

	private long startTime;

	private long elapsed;

	public long getTimeElapsedInMillis() {
		return elapsed;
	}

	public void start() {
		if (startTime != 0) {
            throw new IllegalStateException("StopWatch is already started");
        }
		startTime = System.currentTimeMillis();//.nanoTime();
		elapsed = 0;

	}

	public void stop() {
		if (startTime == 0) {
            throw new IllegalStateException("StopWatch is not started");
        }
		elapsed = System.currentTimeMillis()-startTime;// (System.nanoTime() - startTime) / 1000000L;
		startTime = 0;

	}

}
