package com.fatwire.cs.profiling.servlet.filter;

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
import javax.servlet.http.HttpSessionContext;

public class SessionLessSatelliteFilter implements Filter {
    ServletContext ctx;

    public void destroy() {
        ctx = null;

    }

    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        HttpServletRequest r;

    }

    public void init(FilterConfig filterConfig) throws ServletException {
        ctx = filterConfig.getServletContext();
    }

    static class NoSessionResponseWrapper extends HttpServletRequestWrapper {
        private HttpSession session;

        private ServletContext ctx;

        /**
         * @param response
         */
        public NoSessionResponseWrapper(HttpServletRequest response,
                ServletContext ctx) {
            super(response);
            this.ctx = ctx;
        }

        /* (non-Javadoc)
         * @see javax.servlet.http.HttpServletRequestWrapper#getSession()
         */
        @Override
        public HttpSession getSession() {
            return session;
        }

        /* (non-Javadoc)
         * @see javax.servlet.http.HttpServletRequestWrapper#getSession(boolean)
         */
        @Override
        public HttpSession getSession(boolean create) {
            if (!create)
                return session;
            session = new HttpSession() {

                Map<String, Object> attributes = new HashMap<String, Object>();

                long start = System.currentTimeMillis();

                public Object getAttribute(String name) {
                    return attributes.get(name);
                }

                public Enumeration getAttributeNames() {
                    return Collections.enumeration(attributes.keySet());

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
                    return 300;
                }

                public ServletContext getServletContext() {

                    return ctx;
                }

                public HttpSessionContext getSessionContext() {
                    return null;
                }

                public Object getValue(String name) {

                    return attributes.get(name);
                }

                public String[] getValueNames() {
                    return attributes.keySet().toArray(new String[0]);
                }

                public void invalidate() {
                    // TODO Auto-generated method stub

                }

                public boolean isNew() {
                    return true;
                }

                public void putValue(String name, Object value) {
                    attributes.put(name, value);

                }

                public void removeAttribute(String name) {
                    attributes.remove(name);

                }

                public void removeValue(String name) {
                    attributes.remove(name);

                }

                public void setAttribute(String name, Object value) {
                    attributes.put(name, value);

                }

                public void setMaxInactiveInterval(int interval) {
                    // TODO Auto-generated method stub

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
