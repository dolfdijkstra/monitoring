package com.fatwire.gst.web.servlet.profiling.servlet;

import javax.servlet.http.HttpServletRequest;

public class Util {
    private Util() {
    }

    /**
     * Construct a String representation of this request
     * 
     * @param request
     * @return the url from the request
     */
    public static String getFullUrl(final HttpServletRequest request) {

        final StringBuilder b = new StringBuilder(request.getMethod()).append(
                '|').append(request.getRequestURI());
        if (request.getQueryString() != null) {
            b.append('?').append(request.getQueryString());
        }
        return b.toString();

    }
}