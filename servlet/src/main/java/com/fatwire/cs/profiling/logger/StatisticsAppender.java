/**
 * 
 */
package com.fatwire.cs.profiling.logger;

import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.LoggingEvent;

class StatisticsAppender extends AppenderSkeleton {
    static final String TIME_DEBUG = "com.fatwire.logging.cs.time";

    private Pattern pagePattern = Pattern
            .compile("Execute time  Hours: (\\d{1,}) Minutes: (\\d{1,}) Seconds: (\\d{1,}):(\\d{3})");

    private Pattern elementPattern = create("element", true);

    private Pattern preparedStatementPattern = create("prepared statement",
            false);

    private Pattern queryPattern = create("query", false); // select,insert,delete,update

    private Pattern updatePattern = create("update statement", true);

    private Pattern queryPatternWithDot = create("query", true);

    private ConcurrentHashMap<String, Stat> stats = new ConcurrentHashMap<String, Stat>();

    private MBeanServer server;

    public Stat[] getStats() {
        synchronized (stats) {
            return stats.values().toArray(new Stat[0]);
        }
    }

    //@Override
    protected void append(LoggingEvent event) {
        if (event == null)
            return;
        if (TIME_DEBUG.equals(event.getLoggerName())) {
            // it's ours
            if (event.getMessage() != null) {
                try {
                    parseIt(String.valueOf(event.getMessage()));
                } catch (Exception e) {
                    LogLog.debug(e.getMessage(), e);
                    ;
                }
            }
        }
    }

    //@Override
    public void close() {
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

    private void parseIt(String s) throws Exception {

        long[] pr = this.pageResult(pagePattern.matcher(s));
        if (pr.length == 1) {
            update("page", pr[0]);
            return;
        }
        String[] r = result(elementPattern.matcher(s));
        if (r.length == 2) {
            update("element", r[0], Long.parseLong(r[1]));
            return;
        }
        r = result(preparedStatementPattern.matcher(s));
        if (r.length == 2) {
            update("sql", getStatementType(r[0]), Long.parseLong(r[1]));
            return;

        }
        r = result(queryPattern.matcher(s));
        if (r.length == 2) {
            update("sql", getStatementType(r[0]), Long.parseLong(r[1]));
            return;

        }
        r = result(queryPatternWithDot.matcher(s));
        if (r.length == 2) {
            update("sql", getStatementType(r[0]), Long.parseLong(r[1]));
            return;

        }
        r = result(updatePattern.matcher(s));
        if (r.length == 2) {
            update("sql", getStatementType(r[0]), Long.parseLong(r[1]));
            return;

        }

    }

    private void update(String type, String subType, long time) {
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
                name = new ObjectName("com.fatwire.cs:type=StatFromLogger,group="
                        + type + (subType != null ? ",subType=" + subType : ""));
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

    private void update(String type, long time) {
        update(type, null, time);

    }

    private String getStatementType(String s) {

        int t = s == null ? -1 : s.trim().indexOf(" ");
        if (t != -1) {
            return s.substring(0, t).toLowerCase();
        }
        return "unknown";
    }

    private Pattern create(String type, boolean dot) {
        return Pattern.compile("Executed " + type + " (.+?) in (\\d{1,})ms"
                + (dot ? "." : ""), Pattern.DOTALL);
    }

    private long[] pageResult(Matcher m) {
        long[] r = new long[0];
        if (m.matches()) {
            MatchResult mr = m.toMatchResult();
            if (mr.groupCount() == 4) {
                long t = Long.parseLong(mr.group(1)) * (3600000L);
                t += Long.parseLong(mr.group(2)) * (60000L);
                t += Long.parseLong(mr.group(3)) * (1000L);
                t += Long.parseLong(mr.group(4));
                r = new long[1];
                r[0] = t;

            }

        }
        return r;
    }

    private String[] result(Matcher m) {
        String[] r = new String[0];
        if (m.matches()) {
            MatchResult mr = m.toMatchResult();
            r = new String[mr.groupCount()];
            for (int i = 0; i < mr.groupCount(); i++) {
                r[i] = mr.group(i + 1);

            }
        }
        return r;
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