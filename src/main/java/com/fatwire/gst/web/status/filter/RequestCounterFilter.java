package com.fatwire.gst.web.status.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.fatwire.gst.web.status.RequestCounter;

public class RequestCounterFilter implements Filter /*extends RunOnceFilter*/{
    private RequestCounter requestCounter;

    public void destroy() {
        requestCounter = null;

    }

    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        requestCounter.start((HttpServletRequest) request);
        try {
            chain.doFilter(request, response);
        } finally {
            requestCounter.end((HttpServletRequest) request);
        }
    }

    /**
     * @return the requestCounter
     */
    public RequestCounter getRequestCounter() {
        return requestCounter;
    }

    /**
     * @param requestCounter the requestCounter to set
     */
    public void setRequestCounter(RequestCounter requestCounter) {
        this.requestCounter = requestCounter;
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        //do nothing        
    }

}
