package com.fatwire.cs.profiling.servlet.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fatwire.cs.profiling.servlet.Util;

public class TimerFilter extends HttpFilter implements Filter {
    public void destroy() {
        // TODO Auto-generated method stub

    }
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
            log.info(Long.toString(end - start) + " for "
                    + url);
        }

    }

    public void init(FilterConfig arg0) throws ServletException {
        // TODO Auto-generated method stub

    }

}
