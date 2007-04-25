package com.fatwire.cs.monitor.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Authenticator {

    public boolean authenticate(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException;

}