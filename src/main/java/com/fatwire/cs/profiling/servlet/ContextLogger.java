package com.fatwire.cs.profiling.servlet;

import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ContextLogger implements ServletContextListener {
    private Log log = LogFactory.getLog(this.getClass());

    /* (non-Javadoc)
     * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent event) {
        ServletContext sc = event.getServletContext();
        log.info("ServerInfo: " + sc.getServerInfo());
        log.info("MajorVersion: " + sc.getMajorVersion());
        log.info("MinorVersion: " + sc.getMinorVersion());
        log.info("ServletContextName: " + sc.getServletContextName());

        for (Enumeration e = sc.getInitParameterNames(); e.hasMoreElements();) {
            String name = (String) e.nextElement();
            log.info("Init-Param: " + name + "=" + sc.getInitParameter(name));

        }
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent event) {

        log.info("contextDestroyed: " + event.toString());

    }

}
