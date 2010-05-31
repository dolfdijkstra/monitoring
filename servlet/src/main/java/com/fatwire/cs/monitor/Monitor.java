package com.fatwire.cs.monitor;

public interface Monitor {

	public String getName();

	public Statistics getStatistics();

	public Monitor getParent();
}
