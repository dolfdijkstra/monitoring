package com.fatwire.cs.spring.controllers;

import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class JVMController implements Controller {
    private final Log log = LogFactory.getLog(this.getClass());

    public ModelAndView handleRequest(final HttpServletRequest request,
            final HttpServletResponse response) throws Exception {
        log.trace("handleRequest");
        final OperatingSystemMXBean osMXBean = ManagementFactory
                .getOperatingSystemMXBean();

        final PrintWriter out = response.getWriter();
        out.println("<html><head><title>JVM Info</title>");
        out.println("<style type=\"text/css\">");
        out.println("<!--");
        out.println("table.jvm { font-size: 10pt;");
        out.println(" }");
        out.println("table.products { font-size: 10pt;");
        out.println(" }");
        out.println("h2 { font-size: 16pt;");
        out.println(" }");
        out.println(".notfound { color: red;");
        out.println(" }");

        out.println("-->");

        out.println("</head><body>");

        out.print("<h2>JVM Information</h2>");
        out.print("<table class=\"jvm\">");
        out.print("<tr>");
        out.print("<th>Resource Name</th>");
        out.print("<th>Resource Value</th>");
        out.print("</tr>");
        out.println("<tr class=\"header\"><td colspan=\"2\">Operating System</td></tr>");
        out
                .println("<tr><td>name</td><td>" + osMXBean.getName()
                        + "</td></tr>");
        out.println("<tr><td>version</td><td>" + osMXBean.getVersion()
                + "</td></tr>");
        out.println("<tr><td>architecture</td><td>" + osMXBean.getArch()
                + "</td></tr>");
        out.println("<tr><td>available processors</td><td>"
                + osMXBean.getAvailableProcessors() + "</td></tr>");
        
        final MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        out.println("<tr class=\"header\"><td colspan=\"2\">Memory</td></tr>");
        final long used_mem = memoryMXBean.getHeapMemoryUsage().getUsed();

        final long max_mem = memoryMXBean.getHeapMemoryUsage().getMax();
        final long free_mem = max_mem - used_mem;

        out.println("<tr><td>name</td><td>"
                + memoryMXBean.getHeapMemoryUsage().getInit() + "</td></tr>");
        out.println("<tr><td>max</td><td>" + max_mem + "</td></tr>");
        out.println("<tr><td>used</td><td>" + used_mem + "</td></tr>");
        out.println("<tr><td>free</td><td>" + free_mem + "</td></tr>");
        out.println("<tr><td>commited</td><td>"
                + memoryMXBean.getHeapMemoryUsage().getCommitted()
                + "</td></tr>");

        final RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();

        out.println("<tr class=\"header\"><td colspan=\"2\">Runtime</td></tr>");
        out.println("<tr><td>name</td><td>" + runtimeMXBean.getName()
                + "</td></tr>");
        out.println("<tr><td>spec name</td><td>" + runtimeMXBean.getSpecName()
                + "</td></tr>");
        out.println("<tr><td>spec vendor</td><td>"
                + runtimeMXBean.getSpecVendor() + "</td></tr>");
        out.println("<tr><td>spec version</td><td>"
                + runtimeMXBean.getSpecVersion() + "</td></tr>");
        out.println("<tr><td>start time</td><td>"
                + runtimeMXBean.getStartTime() + "</td></tr>");
        out.println("<tr><td>uptime</td><td>" + runtimeMXBean.getUptime()
                + "</td></tr>");
        out.println("<tr><td>VM name</td><td>" + runtimeMXBean.getVmName()
                + "</td></tr>");
        out.println("<tr><td>VM vendor</td><td>" + runtimeMXBean.getVmVendor()
                + "</td></tr>");
        out.println("<tr><td>VM version</td><td>"
                + runtimeMXBean.getVmVersion() + "</td></tr>");
        out.println("<tr><td>management spec version</td><td>"
                + runtimeMXBean.getManagementSpecVersion() + "</td></tr>");

        out.print("</body></html>");
        return null;
    }

}