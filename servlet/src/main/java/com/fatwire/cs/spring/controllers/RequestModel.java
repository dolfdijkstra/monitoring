/**
 * 
 */
package com.fatwire.cs.spring.controllers;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

public class RequestModel {
    private final HttpServletRequest request;

    private final ServletContext context;

    private final ServletConfig config;

    /**
     * @param request
     * @param context
     * @param config
     */
    public RequestModel(final HttpServletRequest request,
            final ServletContext context, final ServletConfig config) {
        super();
        this.request = request;
        this.context = context;
        this.config = config;
    }

    /**
     * @return the config
     */
    public ServletConfig getConfig() {
        return config;
    }

    /**
     * @return the context
     */
    public ServletContext getContext() {
        return context;
    }

    /**
     * @return the request
     */
    public HttpServletRequest getRequest() {
        return request;
    }

}