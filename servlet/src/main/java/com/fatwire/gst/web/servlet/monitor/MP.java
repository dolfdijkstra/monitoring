package com.fatwire.gst.web.servlet.monitor;

public class MP {

	
	public static final MonitorPool getMonitorPool(){
		return MonitorPoolFactory.getFactory().getMonitorPool();

	}
}
