package com.fatwire.cs.spring.controllers;

import java.io.PrintWriter;
import java.net.UnknownHostException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.fatwire.cs.profiling.jvm.LockDumper;

public class JvmLockController implements Controller {

    private final Log log = LogFactory.getLog(this.getClass());

    private final LockDumper dumper;

    private final String host;

    /**
     * 
     */
    public JvmLockController() {
        super();
        dumper = new LockDumper();
        String tmp;
        try {
            tmp = java.net.InetAddress.getLocalHost().getHostName();
        } catch (final UnknownHostException e) {
            tmp = "unknown";
            log.error(e);
        }
        host = tmp;
    }

    public ModelAndView handleRequest(final HttpServletRequest request,
            final HttpServletResponse response) throws Exception {
        final PrintWriter writer = response.getWriter();
        final String title = "Lock Dump from " + host + " at " + new Date();
        writer.write("<html><head><title>");
        writer.write(title);
        writer.write("</title><head><body>");
        writer.write("<h1>");
        writer.write(title);
        writer.write("</h1>");
        writer.write("<pre>");
        final StringBuilder sb = new StringBuilder();
        dumper.createThreadDump(sb);
        writer.write(sb.toString());

        writer.write("</pre></body></html>");
        return null;
    }

}
