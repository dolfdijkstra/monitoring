package com.fatwire.cs.monitor;

public interface StopWatch {
	
	
	public void start();
	
	public void stop();
	
	/**
	 * 
	 * 
	 * @return a long representing the time elapsed between start and stop in milliseconds
	 */
	
	public long getTimeElapsedInMillis();

}
