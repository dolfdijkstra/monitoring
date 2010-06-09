package com.fatwire.gst.web.status;

import java.lang.management.ManagementFactory;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;

import com.fatwire.gst.web.status.jmx.StatusCounter;

public class StatusRequestListener implements ServletRequestListener,
        ServletContextListener {
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
        } catch (InstanceNotFoundException e) {
            sce.getServletContext().log(e.getMessage(), e);
        } catch (MBeanRegistrationException e) {
            sce.getServletContext().log(e.getMessage(), e);
        }

    }

    public void contextInitialized(ServletContextEvent sce) {
        requestCounter = new RequestCounter(sce.getServletContext()
                .getServletContextName());

        try {
            name = new ObjectName("com.fatwire.cs:type=RequestCounter");
            ManagementFactory.getPlatformMBeanServer().registerMBean(
                    new StatusCounter(requestCounter), name);
        } catch (InstanceAlreadyExistsException e) {
            sce.getServletContext().log(e.getMessage(), e);
        } catch (MBeanRegistrationException e) {
            sce.getServletContext().log(e.getMessage(), e);
        } catch (NotCompliantMBeanException e) {
            sce.getServletContext().log(e.getMessage(), e);
        } catch (MalformedObjectNameException e) {
            sce.getServletContext().log(e.getMessage(), e);
        } catch (NullPointerException e) {
            sce.getServletContext().log(e.getMessage(), e);
        }

    }

}
