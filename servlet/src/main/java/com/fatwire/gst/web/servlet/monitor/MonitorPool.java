package com.fatwire.gst.web.servlet.monitor;


public interface MonitorPool extends Iterable<Monitor> {

	/**
	 * get a Stopwatch with a certain name
	 * 
	 * @param name
	 * @return
	 */

	public StopWatch getStopWatch(String name);

	public Monitor getMonitor(String name);

    

}
