package com.fatwire.gst.web.servlet.profiling.logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.impl.Log4JLogger;

public class TimeDebugListener implements ServletContextListener {
    private LifeCycleManager manager;

    public void contextDestroyed(ServletContextEvent sce) {
        if (manager != null) {
            manager.destroy();
        }

    }

    public void contextInitialized(ServletContextEvent sce) {
        if (this.isLog4JEnabled()) {
            sce.getServletContext().log(
                    "enabling Log4JAppenderLifeCycleManager");
            manager = new Log4JAppenderLifeCycleManager();
            manager.init();
        } else {
            sce.getServletContext().log(
                    "not enabling Log4JAppenderLifeCycleManager");

        }
    }

    boolean isLog4JEnabled() {
        try {
            return Log4JLogger.class.isInstance(LogFactory
                    .getLog(COM.FutureTense.Util.ftMessage.TIME_DEBUG));
        } catch (Throwable e) {
            return false;
        }

    }

}
