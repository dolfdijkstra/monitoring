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
import org.apache.log4j.helpers.LogLog;

import com.fatwire.cs.profiling.jmx.UnregisterMBeansCommand;

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

    private void removeMBeans() {
        try {
            UnregisterMBeansCommand
                    .unregister("com.fatwire.cs:type=StatFromTimeDebug,*");
        } catch (MalformedObjectNameException e) {
            LogLog.error(e.getMessage());
        } catch (NullPointerException e) {
            LogLog.error(e.getMessage());
        }
    }

    void detach() {
        if (log != null) {
            log.removeAppender("stats");
        }
    }

}