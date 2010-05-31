package com.fatwire.gst.web.servlet.profiling.logger;

import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.helpers.LogLog;

import com.fatwire.gst.web.servlet.profiling.jmx.UnregisterMBeansCommand;

public class Log4JAppenderLifeCycleManager implements LifeCycleManager {

    private Logger log;

    private Level oldLevel;

    private boolean oldAdditivity;

    /* (non-Javadoc)
     * @see com.fatwire.gst.web.servlet.profiling.logger.LifeCycleManager#init()
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
            StatisticsProvider provider=new StatisticsProvider(server);
            TimeDebugParser parser =  new TimeDebugParser(provider);

            StatisticsAppender a = new StatisticsAppender(provider,parser);
            a.setName("stats");
            //a.setServer(server);
            a.activateOptions();
            log.addAppender(a);
        }

    }

    /* (non-Javadoc)
     * @see com.fatwire.gst.web.servlet.profiling.logger.LifeCycleManager#destroy()
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
                    .unregister("com.fatwire.gst.web.servlet:type=StatFromTimeDebug,*");
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