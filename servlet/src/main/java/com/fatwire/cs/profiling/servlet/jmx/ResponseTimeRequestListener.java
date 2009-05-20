package com.fatwire.cs.profiling.servlet.jmx;

import java.io.UnsupportedEncodingException;
import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;

public class ResponseTimeRequestListener implements ServletRequestListener,
        ServletContextListener {

    private ObjectName objName;

    private ResponseTimeStatistic stat = new ResponseTimeStatistic();

    private ThreadLocal<Long> time = new ThreadLocal<Long>();

    private Map<String, ResponseTimeStatistic> names = new HashMap<String, ResponseTimeStatistic>();

    public void requestDestroyed(ServletRequestEvent event) {
        if (event.getServletRequest() instanceof HttpServletRequest) {
            HttpServletRequest request = (HttpServletRequest) event
                    .getServletRequest();
            long t = time.get();
            if (t > 0) {
                long elapsed = System.nanoTime() - t;
                stat.signal(request, elapsed);
                String name = extractName(request);
                ResponseTimeStatistic x = names.get(name);
                if (x == null) {
                    x = new ResponseTimeStatistic();

                    names.put(name, x);
                    try {
                        ManagementFactory.getPlatformMBeanServer()
                                .registerMBean(
                                        x,
                                        ObjectName.getInstance(this.objName
                                                + name));
                    } catch (InstanceAlreadyExistsException e) {
                        event.getServletContext().log(e.getMessage(), e);
                    } catch (MBeanRegistrationException e) {
                        event.getServletContext().log(e.getMessage(), e);
                    } catch (NotCompliantMBeanException e) {
                        event.getServletContext().log(e.getMessage(), e);
                    } catch (MalformedObjectNameException e) {
                        event.getServletContext().log(e.getMessage(), e);
                    } catch (NullPointerException e) {
                        event.getServletContext().log(e.getMessage(), e);
                    }
                }
                x.signal(request, elapsed);

            }
        }
        time.set(0L);

    }

    String extractName(HttpServletRequest request) {
        StringBuilder b = new StringBuilder(",path=")
                .append(request.getRequestURI());
        if (request.getQueryString() != null) {
            for (String part : request.getQueryString().split("&")) {
                if (part.startsWith("pagename=")) {
                    b.append(',');
                    try {
                        b.append(java.net.URLDecoder.decode(part,"UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        b.append(part);
                    }
                } else if (part.startsWith("blobtable=")) {
                    b.append(',').append(part);
                }
            }

        }
        return b.toString();
    }

    public void requestInitialized(ServletRequestEvent event) {
        if (event.getServletRequest() instanceof HttpServletRequest)
            time.set(System.nanoTime());

    }

    @SuppressWarnings("unchecked")
    public void contextDestroyed(ServletContextEvent sce) {
        time = null;
        try {
            MBeanServer s = ManagementFactory.getPlatformMBeanServer();
            Set<ObjectName> names = s.queryNames(ObjectName.getInstance(objName
                    + "*"), null);
            if (names != null) {
                for (ObjectName o : names) {
                    s.unregisterMBean(o);
                }
            }

        } catch (InstanceNotFoundException e) {
            sce.getServletContext().log(e.getMessage(), e);
        } catch (MBeanRegistrationException e) {
            sce.getServletContext().log(e.getMessage(), e);
        } catch (MalformedObjectNameException e) {
            sce.getServletContext().log(e.getMessage(), e);
        } catch (NullPointerException e) {
            sce.getServletContext().log(e.getMessage(), e);
        }

    }

    public void contextInitialized(ServletContextEvent sce) {
        sce.getServletContext().log("contextInitialized " + sce.getServletContext().getServletContextName());
        try {
            objName = new ObjectName(
                    "com.fatwire.cs:type=ResponseTimeStatistic");
            ManagementFactory.getPlatformMBeanServer().registerMBean(stat,
                    objName);
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
