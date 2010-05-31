package com.fatwire.gst.web.servlet.monitor;

import java.io.IOException;
import java.io.InputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Properties;

import com.fatwire.gst.web.servlet.monitor.simple.SimpleMonitorPoolFactory;

public class MonitorPoolFactoryConfigurator {

	public static final String PROP_NAME = "MonitorPoolFactoryConfigurator.properties";

	/**
	 * read config file and do the hard work for load and configure the correct
	 * MonitorPoolFactory Currenly does not do this, and returs a
	 * SimpleMonitorPoolFactory
	 * 
	 * @return
	 */
	MonitorPoolFactory configure() {
		return create();
	}

	MonitorPoolFactory create() {
		Properties p;

		final InputStream in = AccessController
				.doPrivileged(new PrivilegedAction<InputStream>() {

					public InputStream run() {
						final ClassLoader cl = Thread.currentThread()
								.getContextClassLoader();
						return cl.getResourceAsStream(MonitorPoolFactoryConfigurator.PROP_NAME);
					}

				});
		if (in == null) {
			return getDefault();
		}
		p = new Properties();
		try {
			p.load(in);
		} catch (final IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (final Exception ignore) {
				// ignore
			}
		}
		final String x = p.getProperty("monitorpoolfactory.class");
		if (x == null) {
			return getDefault();
		}
		Class<?> c;
		try {
			c = Class.forName(x);
			if (c.isAssignableFrom(MonitorPoolFactory.class)){
				return (MonitorPoolFactory)c.newInstance();
			}

		} catch (final ClassNotFoundException e) {
			e.printStackTrace();
		} catch (final InstantiationException e) {
			e.printStackTrace();
		} catch (final IllegalAccessException e) {
			e.printStackTrace();
		}
		return getDefault();
		
	}
	MonitorPoolFactory getDefault(){
		return new SimpleMonitorPoolFactory();
	}
}
