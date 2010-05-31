/**
 * 
 */
package com.fatwire.gst.web.servlet.profiling.logger;

import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.LoggingEvent;

class StatisticsAppender extends BaseAppender {
    static final String TIME_DEBUG = "com.fatwire.logging.cs.time";

    private StatisticsProvider provider;

    /**
     * @param provider
     * @param parser
     */
    public StatisticsAppender(StatisticsProvider provider,
            TimeDebugParser parser) {
        super();
        this.provider = provider;
        this.parser = parser;
    }

    private TimeDebugParser parser;// = new TimeDebugParser(this);

    public Stat[] getStats() {
        return provider.getStats();
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
        provider.close();
    }

    //@Override
    public boolean requiresLayout() {
        return false;
    }

}