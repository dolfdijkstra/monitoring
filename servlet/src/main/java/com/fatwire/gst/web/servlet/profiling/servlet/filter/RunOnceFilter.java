package com.fatwire.gst.web.servlet.profiling.servlet.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * The filter is executed once per request, assuming that the whole request is run by the same thread.
 * 
 * @author Dolf.Dijkstra
 * @since Nov 12, 2008
 */
public abstract class RunOnceFilter extends HttpFilter {

    private final ThreadLocal<Boolean> runOnce = new ThreadLocal<Boolean>() {

        /* (non-Javadoc)
         * @see java.lang.ThreadLocal#initialValue()
         */
        @Override
        protected Boolean initialValue() {
            return false;
        }

    };

    @Override
    protected final void doFilter(HttpServletRequest request,
            HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (!runOnce.get()) {
            runOnce.set(true);
            try {
                doFilterOnce(request, response, chain);
            } finally {
                runOnce.set(false);
            }
        } else {
            chain.doFilter(request, response);
        }
    }

    protected abstract void doFilterOnce(HttpServletRequest request,
            HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException;
}
