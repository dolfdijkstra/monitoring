package com.fatwire.cs.monitor;


public interface MonitorPool extends Iterable {

	/**
	 * get a Stopwatch with a certain name
	 * 
	 * @param name
	 * @return
	 */

	public StopWatch getStopWatch(String name);

	public Monitor getMonitor(String name);

    

}
