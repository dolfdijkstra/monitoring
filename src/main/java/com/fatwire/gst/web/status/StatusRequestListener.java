package com.fatwire.gst.web.status;

import java.lang.management.ManagementFactory;

import javax.management.ObjectName;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fatwire.gst.web.status.jmx.StatusCounter;

public class StatusRequestListener implements ServletRequestListener,
        ServletContextListener {

    private Log log = LogFactory.getLog(StatusRequestListener.class);

    private RequestCounter requestCounter;

    private ObjectName name;

    public void requestDestroyed(ServletRequestEvent event) {
        if (event.getServletRequest() instanceof HttpServletRequest)
            requestCounter.end((HttpServletRequest) event.getServletRequest());

    }

    public void requestInitialized(ServletRequestEvent event) {
        if (event.getServletRequest() instanceof HttpServletRequest)
            requestCounter
                    .start((HttpServletRequest) event.getServletRequest());

    }

    /**
     * @return the requestCounter
     */
    public RequestCounter getRequestCounter() {
        return requestCounter;
    }

    /**
     * @param requestCounter the requestCounter to set
     */
    public void setRequestCounter(RequestCounter requestCounter) {
        this.requestCounter = requestCounter;
    }

    public void contextDestroyed(ServletContextEvent sce) {
        requestCounter = null;
        try {
            ManagementFactory.getPlatformMBeanServer().unregisterMBean(name);
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
        }

    }

    public void contextInitialized(ServletContextEvent sce) {
        requestCounter = new RequestCounter(sce.getServletContext()
                .getServletContextName());

        try {
            name = new ObjectName("com.fatwire.gst:type=RequestCounter");
            ManagementFactory.getPlatformMBeanServer().registerMBean(
                    new StatusCounter(requestCounter), name);
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
        }

    }

}
