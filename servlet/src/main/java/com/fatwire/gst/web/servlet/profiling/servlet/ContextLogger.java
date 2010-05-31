package com.fatwire.gst.web.servlet.profiling.servlet;

import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ContextLogger implements ServletContextListener {
    private final Log log = LogFactory.getLog(this.getClass());

    /* (non-Javadoc)
     * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
     */
    @SuppressWarnings("unchecked")
    public void contextInitialized(final ServletContextEvent event) {
        final ServletContext sc = event.getServletContext();
        log.info("ServerInfo: " + sc.getServerInfo());
        log.info("MajorVersion: " + sc.getMajorVersion());
        log.info("MinorVersion: " + sc.getMinorVersion());
        log.info("ServletContextName: " + sc.getServletContextName());

        for (final Enumeration<String> e = sc.getInitParameterNames(); e
                .hasMoreElements();) {
            final String name = e.nextElement();
            log.info("Init-Param: " + name + "=" + sc.getInitParameter(name));

        }
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
     */
    public void contextDestroyed(final ServletContextEvent event) {

        log.info("contextDestroyed: " + event.toString());

    }

}
