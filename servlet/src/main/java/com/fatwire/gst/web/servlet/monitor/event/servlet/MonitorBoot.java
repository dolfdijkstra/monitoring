package com.fatwire.gst.web.servlet.monitor.event.servlet;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fatwire.gst.web.servlet.monitor.event.FetchEventDispatcher;

public class MonitorBoot implements ServletContextListener {
    private final Log log = LogFactory.getLog(getClass());

    private PerformanceMonitor monitor;

    private ObjectName objectName;

    public MonitorBoot() {
        try {
            objectName = new ObjectName("com.fatwire.gst.web.servlet.satellite", "name",
                    "RSSFetchMonitor");
        } catch (final MalformedObjectNameException e) {
            log.error(e.getMessage(), e);

        }
    }

    public void contextDestroyed(final ServletContextEvent event) {
        FetchEventDispatcher.getInstance().removeFetchEventListener(monitor);
        monitor = null;
        final MBeanServer server = MBeanServerFactory.createMBeanServer();
        try {
            server.unregisterMBean(objectName);
        } catch (final InstanceNotFoundException e) {
            log.error(e.getMessage(), e);
        } catch (final MBeanRegistrationException e) {
            log.error(e.getMessage(), e);
        }

    }

    public void contextInitialized(final ServletContextEvent event) {
        monitor = new PerformanceMonitor("RSSFetchMonitor");
        FetchEventDispatcher.getInstance().addFetchEventListener(monitor);
        if (objectName != null) {
            final MBeanServer server = MBeanServerFactory.createMBeanServer();
            try {
                server.registerMBean(monitor, objectName);
            } catch (final InstanceAlreadyExistsException e) {
                log.error(e.getMessage(), e);
            } catch (final MBeanRegistrationException e) {
                log.error(e.getMessage(), e);
            } catch (final NotCompliantMBeanException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

}
