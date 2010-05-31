package com.fatwire.gst.web.servlet.monitor.event.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fatwire.gst.web.servlet.monitor.event.FetchEvent;
import com.fatwire.gst.web.servlet.monitor.event.FetchEventListener;
import com.fatwire.gst.web.servlet.profiling.servlet.filter.HttpFilter;

public class EventFilter extends HttpFilter implements Filter {

    private FetchEventListener listener;

    @Override
    protected void doFilter(final HttpServletRequest request,
            final HttpServletResponse response, final FilterChain chain)
            throws IOException, ServletException {
        final long start = System.currentTimeMillis();

        chain.doFilter(request, response);
        final FetchEvent event = new FetchEvent(request, start, System
                .currentTimeMillis(), request.getRequestURL().toString());
        listener.fetchPerformed(event);

    }

    /**
     * @param listener the listener to set
     */
    public void setListener(final FetchEventListener listener) {
        this.listener = listener;
    }

}
