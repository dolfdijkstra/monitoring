package com.fatwire.gst.web.servlet.profiling.servlet.filter.debug;

import java.io.IOException;

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

/**
 * 
 * prints a stack trace to the log file each time the servlet sets a response header or a cookie
 * @author Dolf.Dijkstra
 * @since Jun 27, 2009
 */

public class HeaderStackTraceFilter implements Filter {

    private static Log log = LogFactory.getLog(HeaderStackTraceFilter.class);

    public void destroy() {

        // TODO Auto-generated method stub

    }

    public void doFilter(ServletRequest request, ServletResponse response,

    FilterChain chain) throws IOException, ServletException {

        if (response instanceof HttpServletResponse) {

            chain.doFilter(request, new HttpServletResponseWrapper(

            (HttpServletResponse) response) {

                /* (non-Javadoc)

                 * @see javax.servlet.http.HttpServletResponseWrapper#addCookie(javax.servlet.http.Cookie)

                 */

                @Override
                public void addCookie(Cookie cookie) {

                    log.debug(cookie == null ? "null cookie" : cookie.getName()

                    + " '" + cookie.getValue() + "'", new Exception());

                    super.addCookie(cookie);

                }

                /* (non-Javadoc)

                 * @see javax.servlet.http.HttpServletResponseWrapper#addDateHeader(java.lang.String, long)

                 */

                @Override
                public void addDateHeader(String name, long date) {

                    log.debug(name + "=" + date, new Exception());

                    super.addDateHeader(name, date);

                }

                /* (non-Javadoc)

                 * @see javax.servlet.http.HttpServletResponseWrapper#addHeader(java.lang.String, java.lang.String)

                 */

                @Override
                public void addHeader(String name, String value) {

                    log.debug(name + "=" + value, new Exception());

                    super.addHeader(name, value);

                }

                /* (non-Javadoc)

                 * @see javax.servlet.http.HttpServletResponseWrapper#addIntHeader(java.lang.String, int)

                 */

                @Override
                public void addIntHeader(String name, int value) {

                    log.debug(name + "=" + value, new Exception());

                    super.addIntHeader(name, value);

                }

                /* (non-Javadoc)

                 * @see javax.servlet.http.HttpServletResponseWrapper#setDateHeader(java.lang.String, long)

                 */

                @Override
                public void setDateHeader(String name, long date) {

                    log.debug(name + "=" + date, new Exception());

                    super.setDateHeader(name, date);

                }

                /* (non-Javadoc)

                 * @see javax.servlet.http.HttpServletResponseWrapper#setHeader(java.lang.String, java.lang.String)

                 */

                @Override
                public void setHeader(String name, String value) {

                    log.debug(name + "=" + value, new Exception());

                    super.setHeader(name, value);

                }

                /* (non-Javadoc)

                 * @see javax.servlet.http.HttpServletResponseWrapper#setIntHeader(java.lang.String, int)

                 */

                @Override
                public void setIntHeader(String name, int value) {

                    log.debug(name + "=" + value, new Exception());

                    super.setIntHeader(name, value);

                }

            });

        } else {

            chain.doFilter(request, response);

        }

    }

    public void init(FilterConfig filterConfig) throws ServletException {

        // TODO Auto-generated method stub

    }

}