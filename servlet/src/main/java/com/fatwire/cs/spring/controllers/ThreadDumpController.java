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

import com.fatwire.cs.profiling.jvm.ThreadDumper;

public class ThreadDumpController implements Controller {

    private final Log log = LogFactory.getLog(this.getClass());

    private final ThreadDumper dumper;

    private final String host;

    /**
     * 
     */
    public ThreadDumpController() {
        super();
        dumper = new ThreadDumper();
        String tmp;
        try {
            tmp = java.net.InetAddress.getLocalHost().getHostName();
        } catch (final UnknownHostException e) {
            tmp = "unknown";
            log.error(e);
        }
        host = tmp;
    }

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public ModelAndView handleRequest(final HttpServletRequest request,
            final HttpServletResponse response) throws Exception {
        final PrintWriter writer = response.getWriter();
        final String title = "Thread Dump from " + host + " at " + new Date();
        writer.write("<html><head><title>" + title + "</title><head><body>");
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
