package com.fatwire.gst.web.servlet.profiling.servlet.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * Http servlet filter to drop the http response Connection header. 
 * If the web-app would set this header inappropriately 
 * this filter would help to let the servlet container manage the http connection.
 * 
 * 
 * 
 * @author Dolf.Dijkstra
 * 
 */

public class DropConnectionHeaderFilter implements Filter {
    static private Log log = LogFactory
            .getLog(DropConnectionHeaderFilter.class);

    public void destroy() {

    }

    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        if (response instanceof HttpServletResponse) {
            if (log.isTraceEnabled()) {
                log.trace("executing " + request.getParameterMap());
            }

            chain.doFilter(request, new DropConnectionHeaderWrapper(
                    (HttpServletResponse) response));
        } else {
            if (log.isTraceEnabled()) {
                log.trace("chaining " + request.getParameterMap());
            }

            chain.doFilter(request, response);
        }

    }

    public void init(FilterConfig filterConfig) throws ServletException {
    }

    static class DropConnectionHeaderWrapper extends HttpServletResponseWrapper {

        public DropConnectionHeaderWrapper(HttpServletResponse response) {
            super(response);
        }

        boolean shouldSetHeader(String name) {
            if (log.isTraceEnabled()) {
                log.trace("Testing: " + name);
            }
            return !"Connection".equalsIgnoreCase(name);
        }

        /* (non-Javadoc)
         * @see javax.servlet.http.HttpServletResponseWrapper#addHeader(java.lang.String, java.lang.String)
         */
        @Override
        public void addHeader(String name, String value) {
            log.trace("addHeader: " + name + ":" + value);
            if (shouldSetHeader(name))
                super.addHeader(name, value);
            else
                log.debug("dropping header " + name);

        }

        /* (non-Javadoc)
         * @see javax.servlet.http.HttpServletResponseWrapper#setHeader(java.lang.String, java.lang.String)
         */
        @Override
        public void setHeader(String name, String value) {
            log.trace("setHeader: " + name + ":" + value);
            if (shouldSetHeader(name))
                super.setHeader(name, value);
            else
                log.debug("dropping header " + name);
        }

        /* (non-Javadoc)
         * @see javax.servlet.http.HttpServletResponseWrapper#setIntHeader(java.lang.String, int)
         */
        @Override
        public void setIntHeader(String name, int value) {
            log.trace("setintHeader: " + name + ":" + value);
            if (shouldSetHeader(name))
                super.setIntHeader(name, value);
            else
                log.debug("dropping header " + name);

        }

    }
}
