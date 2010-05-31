package com.fatwire.gst.web.servlet.profiling.servlet.filter.debug;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ContentServerDuplicateHeaderFilter implements Filter {
    private static Log log = LogFactory.getLog(ContentServerDuplicateHeaderFilter.class);

    private final Set<String> badHeader = new HashSet<String>();

    private static final String HEADER_LAST_MODIFIED = "last-modified";

    public void destroy() {

    }

    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        if (response instanceof HttpServletResponse) {
            chain.doFilter(request, new ContentServerDuplicateHeaderWrapper(
                    (HttpServletResponse) response));
        } else {
            chain.doFilter(request, response);
        }

    }

    public void init(FilterConfig filterConfig) throws ServletException {
        this.badHeader.add("date");
        this.badHeader.add("server");
        this.badHeader.add("content-length");
        try {
            String bl = filterConfig.getInitParameter("black-list");
            if (bl != null) {
                for (String b : bl.split(";")) {
                    this.badHeader.add(b.toLowerCase().trim());
                }
            }
        } catch (Throwable t) {
            log.error(t.getMessage(), t);
        }
    }

    class ContentServerDuplicateHeaderWrapper extends
            HttpServletResponseWrapper {
        private Set<String> cookieNames = new HashSet<String>();

        boolean lastModSet = false;

        public ContentServerDuplicateHeaderWrapper(HttpServletResponse response) {
            super(response);
        }

        /* (non-Javadoc)
         * @see javax.servlet.http.HttpServletResponseWrapper#addCookie(javax.servlet.http.Cookie)
         */
        @Override
        public void addCookie(Cookie cookie) {
            if (cookie == null)
                return;
            if (cookieNames.contains(cookie.getName())) {
                return;
            }
            cookieNames.add(cookie.getName());
            super.addCookie(cookie);
        }

        /* (non-Javadoc)
         * @see javax.servlet.http.HttpServletResponseWrapper#addDateHeader(java.lang.String, long)
         */
        @Override
        public void addDateHeader(String name, long date) {
            if (shouldSetHeader(name))
                super.addDateHeader(name, date);
        }

        boolean shouldSetHeader(String name) {
            if (HEADER_LAST_MODIFIED.equalsIgnoreCase(name)) {
                if (this.lastModSet) {
                    log
                            .trace("Last-Modified header already set, ignoring this one.");
                    return false;
                }
                lastModSet = true;
            } else if (badHeader.contains(name.toLowerCase())) {
                //nothing
                if (log.isTraceEnabled()) {
                    log.trace("Ignoring header '" + name
                            + "' due to black listing.");
                }
                return false;
            }
            return true;
        }

        /* (non-Javadoc)
         * @see javax.servlet.http.HttpServletResponseWrapper#addHeader(java.lang.String, java.lang.String)
         */
        @Override
        public void addHeader(String name, String value) {
            if (shouldSetHeader(name))
                super.addHeader(name, value);
        }

        /* (non-Javadoc)
         * @see javax.servlet.http.HttpServletResponseWrapper#addIntHeader(java.lang.String, int)
         */
        @Override
        public void addIntHeader(String name, int value) {
            if (shouldSetHeader(name))
                super.addIntHeader(name, value);
        }

        /* (non-Javadoc)
         * @see javax.servlet.http.HttpServletResponseWrapper#setDateHeader(java.lang.String, long)
         */
        @Override
        public void setDateHeader(String name, long date) {
            if (shouldSetHeader(name))
                super.setDateHeader(name, date);
        }

        /* (non-Javadoc)
         * @see javax.servlet.http.HttpServletResponseWrapper#setHeader(java.lang.String, java.lang.String)
         */
        @Override
        public void setHeader(String name, String value) {
            if (shouldSetHeader(name))
                super.setHeader(name, value);
        }

        /* (non-Javadoc)
         * @see javax.servlet.http.HttpServletResponseWrapper#setIntHeader(java.lang.String, int)
         */
        @Override
        public void setIntHeader(String name, int value) {
            if (shouldSetHeader(name))
                super.setIntHeader(name, value);
        }

    }
}