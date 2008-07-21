package com.fatwire.dta.lwa.sensor;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NoContentServlet extends HttpServlet {

    /**
     * 
     */
    private static final long serialVersionUID = 5555204138074455219L;

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(final HttpServletRequest request,
            final HttpServletResponse response) throws ServletException,
            IOException {
        response.addHeader("Cache-Control", "no-store, no-cache, private");
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#getLastModified(javax.servlet.http.HttpServletRequest)
     */
    @Override
    protected long getLastModified(final HttpServletRequest req) {
        return -1;
    }

}