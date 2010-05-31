package com.fatwire.gst.web.servlet.monitor;

public abstract class MonitorPoolFactory {

	private static MonitorPoolFactory factory;

	static public final MonitorPoolFactory getFactory() {
		if (MonitorPoolFactory.factory == null) {
			synchronized (MonitorPoolFactory.class) {
				MonitorPoolFactory.factory = new MonitorPoolFactoryConfigurator().configure();
			}
		}
		return MonitorPoolFactory.factory;
	}

	protected abstract MonitorPool getMonitorPool();

}
