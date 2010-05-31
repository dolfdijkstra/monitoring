package com.fatwire.gst.web.servlet.profiling.servlet.filter;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;



/**
 * Filter that intercepts any session create requests from the web-app, but lets the web-app believe that the session is created. 
 * 
 * In essence it reduces the session scope to the length of the request.
 * 
 * 
 * @author Dolf.Dijkstra
 * @since May 31, 2010
 */


public class SessionLessSatelliteFilter implements Filter {
    ServletContext ctx;

    public void destroy() {
        ctx = null;

    }

    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        HttpServletRequest r = new NoSessionRequestWrapper(
                (HttpServletRequest) request, ctx);
        chain.doFilter(r, response);

    }

    public void init(FilterConfig filterConfig) throws ServletException {
        ctx = filterConfig.getServletContext();
    }

    static class NoSessionRequestWrapper extends HttpServletRequestWrapper {
        private HttpSession session;

        private ServletContext ctx;

        /**
         * @param response
         */
        public NoSessionRequestWrapper(HttpServletRequest request,
                ServletContext ctx) {
            super(request);
            this.ctx = ctx;
        }

        /* (non-Javadoc)
         * @see javax.servlet.http.HttpServletRequestWrapper#getSession()
         */
        @Override
        public HttpSession getSession() {
            return getSession(true);
        }

        /* (non-Javadoc)
         * @see javax.servlet.http.HttpServletRequestWrapper#getSession(boolean)
         */
        @Override
        public HttpSession getSession(boolean create) {
            if (!create)
                return session;
            session = new HttpSession() {

                private Map<String, Object> attributes;

                private long start = System.currentTimeMillis();

                private int interval = 300;

                public Object getAttribute(String name) {
                    return attr().get(name);
                }

                @SuppressWarnings("unchecked")
                public Enumeration getAttributeNames() {
                    return Collections.enumeration(attr().keySet());

                }

                public long getCreationTime() {
                    return start;
                }

                public String getId() {
                    return "123";
                }

                public long getLastAccessedTime() {
                    return start;
                }

                public int getMaxInactiveInterval() {
                    return interval;
                }

                public ServletContext getServletContext() {

                    return ctx;
                }

                @SuppressWarnings("deprecation")
                public javax.servlet.http.HttpSessionContext getSessionContext() {
                    return null;
                }

                public Object getValue(String name) {

                    return attr().get(name);
                }

                private Map<String, Object> attr() {
                    if (attributes == null)
                        attributes = new HashMap<String, Object>();
                    return attributes;
                }

                public String[] getValueNames() {
                    return attr().keySet().toArray(new String[0]);
                }

                public void invalidate() {

                }

                public boolean isNew() {
                    return true;
                }

                public void putValue(String name, Object value) {
                    attr().put(name, value);

                }

                public void removeAttribute(String name) {
                    attr().remove(name);

                }

                public void removeValue(String name) {
                    attr().remove(name);

                }

                public void setAttribute(String name, Object value) {
                    attr().put(name, value);

                }

                public void setMaxInactiveInterval(int interval) {
                    this.interval = interval;

                }

            };
            return session;
        }

        /* (non-Javadoc)
         * @see javax.servlet.http.HttpServletRequestWrapper#isRequestedSessionIdValid()
         */
        @Override
        public boolean isRequestedSessionIdValid() {
            return true;
        }

    }
}