package com.fatwire.cs.profiling.jmx;

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

    }

    public void contextInitialized(final ServletContextEvent event) {
        log.info("contextInitialized");
        cacheManager = new CacheManager();
        try {
            cacheManager.setManagerName( new ObjectName("com.fatwire.cs:type=CacheManager"));
            cacheManager.start();
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
        }
    }

}
