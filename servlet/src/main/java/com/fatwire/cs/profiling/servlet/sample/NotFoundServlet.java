package com.fatwire.cs.profiling.servlet.sample;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NotFoundServlet extends HttpServlet {

    /**
     * 
     */
    private static final long serialVersionUID = 2736023612235775803L;

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
        if (request.getParameter("withbody") != null) {
            response.getWriter().write("This page could not be found");
        }

    }

}
