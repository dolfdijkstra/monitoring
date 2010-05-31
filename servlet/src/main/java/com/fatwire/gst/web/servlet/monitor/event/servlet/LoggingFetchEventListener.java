package com.fatwire.gst.web.servlet.monitor.event.servlet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fatwire.gst.web.servlet.monitor.event.FetchEvent;
import com.fatwire.gst.web.servlet.monitor.event.FetchEventListener;

public class LoggingFetchEventListener implements FetchEventListener {
    private final Log log = LogFactory.getLog(getClass());

    private static final char[] msgPrefix = "fetching took ".toCharArray();

    private static final char[] msgPostfix = " ms. for ".toCharArray();

    public void fetchPerformed(final FetchEvent event) {
        if (log.isDebugEnabled()) {
            final StringBuilder bld = new StringBuilder(64);
            bld.append(LoggingFetchEventListener.msgPrefix).append(
                    String.valueOf(event.getEndTime() - event.getStartTime())
                            .toCharArray());
            bld.append(LoggingFetchEventListener.msgPostfix);
            log.debug(bld.toString());
        }

    }

}
