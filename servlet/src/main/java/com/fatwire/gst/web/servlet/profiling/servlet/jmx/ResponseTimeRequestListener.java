package com.fatwire.gst.web.servlet.profiling.servlet.jmx;

import java.lang.management.ManagementFactory;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;

import com.fatwire.gst.web.servlet.profiling.jmx.UnregisterMBeansCommand;

public class ResponseTimeRequestListener implements ServletRequestListener,
        ServletContextListener {

    private static final String MBEAN_NAME = "com.fatwire.gst.web.servlet:type=ResponseTimeStatistic";

    private NameBuilder nameBuilder = new NameBuilder();

    private ResponseTimeStatistic stat = new ResponseTimeStatistic();

    private ThreadLocal<Measurement> time = new ThreadLocal<Measurement>() {

        /* (non-Javadoc)
         * @see java.lang.ThreadLocal#initialValue()
         */
        @Override
        protected Measurement initialValue() {
            return new Measurement();
        }

    };

    private Map<String, ResponseTimeStatistic> names = new ConcurrentHashMap<String, ResponseTimeStatistic>(
            800, 0.75f, 400);

    public void requestDestroyed(ServletRequestEvent event) {
        if (event.getServletRequest() instanceof HttpServletRequest) {
            HttpServletRequest request = (HttpServletRequest) event
                    .getServletRequest();
            Measurement m = time.get();
            m.stop();

            stat.signal(request, m);
            String name = nameBuilder.extractName(request);
            ResponseTimeStatistic x = names.get(name);
            if (x == null) {
                x = new ResponseTimeStatistic();

                names.put(name, x);
                try {
                    ManagementFactory.getPlatformMBeanServer().registerMBean(x,
                            ObjectName.getInstance(MBEAN_NAME + name));
                } catch (InstanceAlreadyExistsException e) {
                    event.getServletContext().log(e.getMessage(), e);
                } catch (MBeanRegistrationException e) {
                    event.getServletContext().log(e.getMessage(), e);
                } catch (NotCompliantMBeanException e) {
                    event.getServletContext().log(e.getMessage(), e);
                } catch (MalformedObjectNameException e) {
                    event.getServletContext().log(
                            e.getMessage() + " for " + MBEAN_NAME + name, e);
                } catch (NullPointerException e) {
                    event.getServletContext().log(e.getMessage(), e);
                }
            }
            x.signal(request, m);

        }

    }

    public void requestInitialized(ServletRequestEvent event) {
        if (event.getServletRequest() instanceof HttpServletRequest)
            time.get().start();

    }

    public void contextDestroyed(ServletContextEvent sce) {
        time = null;
        try {
            UnregisterMBeansCommand.unregister(MBEAN_NAME + ",*");
        } catch (Exception e) {
            sce.getServletContext().log(e.getMessage(), e);
        }

    }

    public void contextInitialized(ServletContextEvent sce) {
        sce.getServletContext().log(
                "contextInitialized "
                        + sce.getServletContext().getServletContextName());
        try {

            ManagementFactory.getPlatformMBeanServer().registerMBean(stat,
                    ObjectName.getInstance(MBEAN_NAME));
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
