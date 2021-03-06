package com.fatwire.cs.spring.controllers;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.NumberFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.fatwire.cs.monitor.Statistics;
import com.fatwire.cs.monitor.event.servlet.RequestMonitor;

/**
 * Both controller and view
 * @author Dolf.Dijkstra
 * @since 
 */
public class MonitorController implements Controller {

    private RequestMonitor monitor;

    /* (non-Javadoc)
     * @see com.fatwire.cs.monitor.servlet.Controller#process(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public ModelAndView handleRequest(final HttpServletRequest request,
            final HttpServletResponse response) throws Exception {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        final PrintWriter writer = response.getWriter();
        writer.write("<html><head><title>Reporter</title>\r\n");
        writer.write("<style type=\"text/css\">\r\n");
        writer.write("<!-- \r\n");
        writer.write("td { font-size: 8pt;\r\n");
        writer.write("white-space:nowrap;\r\n");
        writer.write(" } \r\n");
        writer.write("-->\r\n");

        writer.write("</head><body>\r\n");
        final NumberFormat formatter = NumberFormat.getInstance();
        formatter.setMaximumFractionDigits(2);
        formatter.setMinimumFractionDigits(0);
        final DateFormat dateFormatter = DateFormat.getInstance();
        
        writer.write("<table>");
        writer.write("<tr>");
        writer.write("<th>");
        writer.write("name");
        writer.write("</th>");
        writer.write("<th>");
        writer.write("count");
        writer.write("</th>");
        writer.write("<th>");
        writer.write("first date");
        writer.write("</th>");
        writer.write("<th>");
        writer.write("last date");
        writer.write("</th>");
        writer.write("<th>");
        writer.write("average");
        writer.write("</th>");

        writer.write("<th>");
        writer.write("min");
        writer.write("</th>");
        writer.write("<th>");
        writer.write("max");
        writer.write("</th>");
        writer.write("<th>");
        writer.write("total");
        writer.write("</th>");
        writer.write("<th>");
        writer.write("std dev");
        writer.write("</th>");

        writer.write("</tr>");

        for (final Statistics stat: monitor.getStatistics()) {

            writer.write("\r\n<tr>");
            writer.write("<td>");
            writer.write(stat.getName());
            writer.write("</td>");
            writer.write("<td style=\"text-align: right\">");
            writer.write(formatter.format(stat.getInvocations()));
            writer.write("</td>");
            writer.write("<td style=\"text-align: right\">");
            writer.write(dateFormatter.format(stat.getFirstDate()));
            writer.write("</td>");
            writer.write("<td style=\"text-align: right\">");
            writer.write(dateFormatter.format(stat.getLastDate()));
            writer.write("</td>");

            writer.write("<td style=\"text-align: right\">");
            writer.write(formatter.format(stat.getAverage()));
            writer.write("</td>");
            writer.write("<td style=\"text-align: right\">");
            writer.write(formatter.format(stat.getMinValue()));
            writer.write("</td>");
            writer.write("<td style=\"text-align: right\">");
            writer.write(formatter.format(stat.getMaxvalue()));
            writer.write("</td>");
            writer.write("<td style=\"text-align: right\">");
            writer.write(formatter.format(stat.getTotal()));
            writer.write("</td>");
            writer.write("<td style=\"text-align: right\">");
            writer.write(formatter.format(stat.getStandardDeviation()));
            writer.write("</td>");

            writer.write("</tr>");

        }
        writer.write("\r\n</table>\r\n");
        writer.write("</body></html>");
        writer.flush();
        return null;
    }

    /**
     * @param monitor the monitor to set
     */
    public void setMonitor(RequestMonitor monitor) {
        this.monitor = monitor;
    }

}
