package com.fatwire.gst.web.servlet.profiling.logger.ft;

import java.lang.management.ManagementFactory;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

import com.fatwire.cs.core.logging.TraditionalLog;
import com.fatwire.gst.web.servlet.profiling.logger.StatisticsProvider;
import com.fatwire.gst.web.servlet.profiling.logger.TimeDebugParser;

public class TraditionalInterceptor extends TraditionalLog {
    private TimeDebugParser parser;

    static StatisticsProvider provider;

    public TraditionalInterceptor(String name) {
        super(name);
        if (name.startsWith(COM.FutureTense.Util.ftMessage.TIME_DEBUG))
            try {
                init();
            } catch (Exception e) {
                e.printStackTrace();
            }

    }

    private void init() throws InstanceAlreadyExistsException,
            MBeanRegistrationException, NotCompliantMBeanException,
            MalformedObjectNameException, NullPointerException {
        if (provider == null) {
            MBeanServer server = ManagementFactory.getPlatformMBeanServer();
            provider = new StatisticsProvider(server);
            server.registerMBean(provider, ObjectName
                    .getInstance(StatisticsProvider.NAME));

        }

        parser = new TimeDebugParser(provider);
    }

    /* (non-Javadoc)
     * @see com.fatwire.gst.web.servlet.core.logging.AbstractLog#debug(java.lang.Object, java.lang.Throwable)
     */
    @Override
    public void debug(Object arg0, Throwable arg1) {
        super.debug(arg0, arg1);
        if (arg0 != null && parser != null) {
            try {
                parser.parseIt(String.valueOf(arg0));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /* (non-Javadoc)
     * @see com.fatwire.gst.web.servlet.core.logging.AbstractLog#debug(java.lang.Object)
     */
    @Override
    public void debug(Object arg0) {
        super.debug(arg0);
        if (arg0 != null && parser != null) {
            try {
                parser.parseIt(String.valueOf(arg0));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /* (non-Javadoc)
     * @see com.fatwire.gst.web.servlet.core.logging.AbstractLog#trace(java.lang.Object, java.lang.Throwable)
     */
    @Override
    public void trace(Object arg0, Throwable arg1) {
        super.trace(arg0, arg1);
        if (arg0 != null && parser != null) {
            try {
                parser.parseIt(String.valueOf(arg0));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /* (non-Javadoc)
     * @see com.fatwire.gst.web.servlet.core.logging.AbstractLog#trace(java.lang.Object)
     */
    @Override
    public void trace(Object arg0) {
        super.trace(arg0);
        if (arg0 != null && parser != null) {
            try {
                parser.parseIt(String.valueOf(arg0));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}
