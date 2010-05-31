package com.fatwire.gst.web.servlet.profiling.servlet.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fatwire.gst.web.servlet.profiling.servlet.Util;

/**
 * 
 * Servlet filter that prints response time to log
 * 
 * @author Dolf.Dijkstra
 * 
 */

public class TimerFilter extends HttpFilter implements Filter {

    /**
     * Prints the time it took to process this request.
     * If you don't want this info, shut down the filter by removing it from the web-app.
     * Otherwise this filter will consume resources.
     * 
     */

    public void doFilter(HttpServletRequest request,
            HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        long start = System.currentTimeMillis();
        String url = Util.getFullUrl(request);
        try {
            chain.doFilter(request, response);
        } finally {
            long end = System.currentTimeMillis();
            log.debug(Long.toString(end - start) + " ms for " + url);
        }

    }

}
