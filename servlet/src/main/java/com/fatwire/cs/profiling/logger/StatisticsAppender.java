/**
 * 
 */
package com.fatwire.cs.profiling.logger;

import java.util.concurrent.ConcurrentHashMap;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.LoggingEvent;

class StatisticsAppender extends BaseAppender implements TimeDebugParser.ParserCallback {
    static final String TIME_DEBUG = "com.fatwire.logging.cs.time";

    private TimeDebugParser parser = new TimeDebugParser(this);

    private ConcurrentHashMap<String, Stat> stats = new ConcurrentHashMap<String, Stat>(
            200, 0.75f, 2); //append is called from asynchronized method

    private MBeanServer server;

    public Stat[] getStats() {
        synchronized (stats) {
            return stats.values().toArray(new Stat[0]);
        }
    }


    protected void append(LoggingEvent event) {
        if (TIME_DEBUG.equals(event.getLoggerName())) {
            // it's ours
            if (event.getMessage() != null) {
                try {
                    parser.parseIt(String.valueOf(event.getMessage()));
                } catch (Exception e) {
                    LogLog.debug(e.getMessage(), e);
                }
            }
        }
    }

    //@Override
    public void close() {
        if (closed)
            return;
        closed = true;
        stats.clear();
        for (Stat s : getStats()) {
            if (s.getName() != null) {
                try {
                    server.unregisterMBean(s.getName());
                } catch (Throwable e) {
                    LogLog.warn(e.getMessage(), e);
                }
            }
        }
        server = null;
    }

    //@Override
    public boolean requiresLayout() {
        return false;
    }


    public void update(String type, String subType, long time) {
        if (time > 3722801423L)
            return; // do not log large values do to bug in CS when turning on time debug.
        Stat s = getStat(type, subType);
        s.update(time);

    }

    private Stat getStat(String type, String subType) {
        String n = subType != null ? (type + "-" + subType) : type;
        Stat s = stats.get(n);
        if (s == null) {
            ObjectName name = null;
            try {
                name = new ObjectName(
                        "com.fatwire.cs:type=StatFromTimeDebug,group="
                                + type
                                + (subType != null ? ",subType=" + subType : ""));
            } catch (MalformedObjectNameException e) {
                LogLog.warn(e.getMessage(), e);
            } catch (NullPointerException e) {
                LogLog.warn(e.getMessage(), e);
            }
            s = new Stat();
            s.setType(type);
            s.setSubType(subType);
            s.setName(name);
            stats.put(n, s);
            if (name != null) {
                try {
                    this.server.registerMBean(s, name);
                } catch (InstanceAlreadyExistsException e) {
                    LogLog.warn(e.getMessage(), e);
                } catch (MBeanRegistrationException e) {
                    LogLog.warn(e.getMessage(), e);
                } catch (NotCompliantMBeanException e) {
                    LogLog.warn(e.getMessage(), e);
                }
            }

        }
        return s;
    }



    /**
     * @return the server
     */
    public MBeanServer getServer() {
        return server;
    }

    /**
     * @param server the server to set
     */
    public void setServer(MBeanServer server) {
        this.server = server;
    }
}