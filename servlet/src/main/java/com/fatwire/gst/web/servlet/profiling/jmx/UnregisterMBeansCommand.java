package com.fatwire.gst.web.servlet.profiling.jmx;

import java.lang.management.ManagementFactory;
import java.util.Set;

import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class UnregisterMBeansCommand {
    private static final Log log = LogFactory.getLog(UnregisterMBeansCommand.class);

    public static void unregister(String query) throws MalformedObjectNameException, NullPointerException {
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = ObjectName.getInstance(query);
        Set<ObjectName> mbeans = server.queryNames(name, null);
        for (ObjectName on : mbeans) {
            try {
                server.unregisterMBean(on);
            } catch (Exception ee) {
                log.error(ee.getMessage(), ee);
            }
        }

    }

}
