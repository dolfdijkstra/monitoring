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
        cacheManager = new CacheManager();
        cacheManager.setDomainName("com.fatwire.contentserver");
        try {
            cacheManager.start();
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
        }
    }

}
