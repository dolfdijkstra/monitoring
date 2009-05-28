package com.fatwire.cs.profiling.logger;

import java.lang.management.ManagementFactory;
import java.util.Set;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class Log4JAppenderLifeCycleManager implements LifeCycleManager {

    private Logger log;

    private Level oldLevel;

    private boolean oldAdditivity;

    /* (non-Javadoc)
     * @see com.fatwire.cs.profiling.logger.LifeCycleManager#init()
     */
    public void init() {
        log = Logger.getLogger(StatisticsAppender.TIME_DEBUG);
        if (log.getAppender("stats") == null) {
            MBeanServer server = ManagementFactory.getPlatformMBeanServer();

            oldLevel = log.getLevel();
            oldAdditivity = log.getAdditivity();
            if (!log.isDebugEnabled()) {
                log.setLevel(Level.DEBUG);
                log.setAdditivity(false);
            }

            StatisticsAppender a = new StatisticsAppender();
            a.setName("stats");
            a.setServer(server);
            a.activateOptions();
            log.addAppender(a);
        }

    }

    /* (non-Javadoc)
     * @see com.fatwire.cs.profiling.logger.LifeCycleManager#destroy()
     */
    public void destroy() {
        if (log != null) {
            log.removeAppender("stats");
            log.setLevel(oldLevel);
            log.setAdditivity(oldAdditivity);
            removeMBeans();

        }
    }

    @SuppressWarnings("unchecked")
    private void removeMBeans() {
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        try {
            Set<ObjectName> names = server.queryNames(new ObjectName(
                    "com.fatwire.cs:type=StatFromTimeDebug,*"), null);
            for (ObjectName name : names) {
                try {
                    server.unregisterMBean(name);
                } catch (InstanceNotFoundException e) {
                    log.warn(e);
                } catch (MBeanRegistrationException e) {
                    log.warn(e);
                }
            }
        } catch (MalformedObjectNameException e) {
            log.error(e);
        } catch (NullPointerException e) {
            log.error(e);
        }
    }

    void detach() {
        if (log != null) {
            log.removeAppender("stats");
        }
    }

}