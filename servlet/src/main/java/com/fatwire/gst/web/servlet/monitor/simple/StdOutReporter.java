package com.fatwire.gst.web.servlet.monitor.simple;

import com.fatwire.gst.web.servlet.monitor.Monitor;
import com.fatwire.gst.web.servlet.monitor.Reporter;

public class StdOutReporter implements Reporter {

	public void report(final Monitor monitor) {
		System.out.println(monitor.getName());
		System.out.println(monitor.getStatistics().getInvocations());
		System.out.println(monitor.getStatistics().getMaxvalue());
		System.out.println(monitor.getStatistics().getMinValue());
		System.out.println(monitor.getStatistics().getAverage());
	}

}
