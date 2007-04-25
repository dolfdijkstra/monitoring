package com.fatwire.cs.profiling.servlet.sample;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BackendSessionServlet extends HttpServlet {

    /**
     * 
     */
    private static final long serialVersionUID = 3309942263154472007L;

    public BackendSessionServlet() {
        // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        Cookie c = new Cookie("CSCOOKIE", Long.toHexString(System
                .currentTimeMillis()));

        response.addCookie(c);
        request.getSession();
        request.getRequestDispatcher("/index.jsp").forward(request, response);

    }
}
