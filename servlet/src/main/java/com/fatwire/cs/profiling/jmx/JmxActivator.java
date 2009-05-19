package com.fatwire.cs.profiling.jmx;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class JmxActivator implements ServletContextListener {
    private final Log log = LogFactory.getLog(this.getClass());

    private CacheManagerMBean cacheManager;

    public void contextDestroyed(final ServletContextEvent event) {
        try {
            cacheManager.shutdown();
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
        }

    }

    public void contextInitialized(final ServletContextEvent event) {
        log.info("JmxActivator.contextInitialized");
        CacheManager cacheManager1 = new CacheManager();
        cacheManager1.setDomainName("com.fatwire.cs");
        cacheManager = cacheManager1;
        try {
            cacheManager.start();
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
        }
    }

}
