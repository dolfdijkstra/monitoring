package com.fatwire.gst.web.servlet.monitor;

public interface Monitor {

	public String getName();

	public Statistics getStatistics();

	public Monitor getParent();
}
