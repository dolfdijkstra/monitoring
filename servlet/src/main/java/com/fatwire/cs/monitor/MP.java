package com.fatwire.cs.monitor;

public class MP {

	
	public static final MonitorPool getMonitorPool(){
		return MonitorPoolFactory.getFactory().getMonitorPool();

	}
}
