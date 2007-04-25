package com.fatwire.cs.profiling.servlet.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class HttpFilter {

    protected Log log = LogFactory.getLog(getClass());

    protected abstract void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException;

    public HttpFilter() {
        super();
    }

    public final void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            doFilter((HttpServletRequest) request,
                    (HttpServletResponse) response, chain);
        } else {
            chain.doFilter(request, response);
        }
    }

}