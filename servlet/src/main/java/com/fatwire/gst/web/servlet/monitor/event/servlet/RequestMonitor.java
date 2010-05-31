package com.fatwire.gst.web.servlet.monitor.event.servlet;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import com.fatwire.gst.web.servlet.monitor.Statistics;
import com.fatwire.gst.web.servlet.monitor.commons.math.SummaryStatisticsAdapter;
import com.fatwire.gst.web.servlet.monitor.event.FetchEvent;
import com.fatwire.gst.web.servlet.monitor.event.FetchEventListener;

public class RequestMonitor implements FetchEventListener {

    private Map<String, SummaryStatisticsAdapter> statistics = new ConcurrentHashMap<String, SummaryStatisticsAdapter>();

    private String[] paramsToListenFor = new String[0];

    public void fetchPerformed(final FetchEvent event) {
        SummaryStatisticsAdapter s = null;
        if (event.getSource() instanceof HttpServletRequest) {
            final HttpServletRequest request = (HttpServletRequest) event
                    .getSource();

            StringBuilder b = new StringBuilder();
            for (String p : paramsToListenFor) {
                if (request.getParameter(p) != null) {
                    if (b.length() > 0) {
                        b.append('&');
                    }
                    b.append(p).append('=').append(request.getParameter(p));
                }
            }
            if (b.length() > 0) {
                s = getInternal(b.toString());
            }
            if (s == null) {
                s = getInternal(request.getRequestURL().toString());
            }

        }
        if (s == null) {
            s = getInternal(event.getUrl());
        }
        if (s != null) {
            s.addValue(event.getEndTime() - event.getStartTime());
        } else {
            Thread.dumpStack();
        }

    }

    SummaryStatisticsAdapter getInternal(final String name) {
        if (name == null) {
            return null;
        }
        SummaryStatisticsAdapter s = statistics.get(name);
        if (s == null) {
            s = new SummaryStatisticsAdapter(name);
            statistics.put(name, s);
        }
        return s;
    }

    public Collection<? extends Statistics> getStatistics() {
        return Collections.unmodifiableCollection(statistics.values());
    }

    /**
     * @param paramsToListenFor the paramsToListenFor to set
     */
    public void setParamsToListenFor(String paramsToListenFor) {
        if (paramsToListenFor != null) {
            this.paramsToListenFor = paramsToListenFor.split(",");
        }
    }

}
