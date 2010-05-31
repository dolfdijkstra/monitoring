package com.fatwire.gst.web.servlet.profiling.jmx;

import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class JmxActivator implements ServletContextListener {
    private final Log log = LogFactory.getLog(this.getClass());

    private CacheManager cacheManager;

    public void contextDestroyed(final ServletContextEvent event) {
        try {
            cacheManager.shutdown();

        } catch (final Exception e) {
            log.error(e.getMessage(), e);
        }
        try {
            UnregisterMBeansCommand
                    .unregister("com.fatwire.gst.web.servlet:type=CacheManager");
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
        }
        try {
            UnregisterMBeansCommand.unregister("com.fatwire.gst.web.servlet:type=Cache,*");
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
        }

    }

    public void contextInitialized(final ServletContextEvent event) {
        log.debug("contextInitialized");
        cacheManager = new CacheManager();
        try {
            cacheManager.start();
            MBeanServer server = ManagementFactory.getPlatformMBeanServer();
            server.registerMBean(cacheManager, new ObjectName(
                    "com.fatwire.gst.web.servlet:type=CacheManager"));

        } catch (final Exception e) {
            log.error(e.getMessage(), e);
        }
    }

}
