package com.fatwire.gst.web.servlet.monitor.servlet.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fatwire.gst.web.servlet.monitor.SW;
import com.fatwire.gst.web.servlet.monitor.StopWatch;
import com.fatwire.gst.web.servlet.profiling.servlet.filter.HttpFilter;

public class StopWatchFilter extends HttpFilter implements Filter {

    protected String[] specialParams;

    public void destroy() {
        // nothing

    }

    protected void doFilter(final HttpServletRequest request,
            final HttpServletResponse response, final FilterChain chain)
            throws IOException, ServletException {

        final String swName = getStopWatchName(request);
        //System.out.println(swName.toString());
        final StopWatch sw = SW.getStopWatch(swName);
        sw.start();
        try {
            chain.doFilter(request, response);
        } finally {
            sw.stop();
        }
    }

    String getStopWatchName(final HttpServletRequest request) {
        final StringBuffer swName = new StringBuffer("request.");
        swName.append(request.getMethod());
        //swName.append(request.getContextPath());
        swName.append(".").append(request.getRequestURI());
        for (int i = 0; i < specialParams.length; i++) {
            final String special = specialParams[i];
            final String value = request.getParameter(special);
            if (value != null) {
                swName.append(".").append(special).append("=").append(value);
            }
        }

        return swName.toString();

    }

    /**
     * Look for a semicolon seperated initParameter called specialParams to add
     * these http request parameters for inclusion in the StopWatch name
     */
    public void init(final FilterConfig filterConfig) throws ServletException {
        final String sp = filterConfig.getInitParameter("specialParams");
        if (sp != null) {
            specialParams = sp.split(";");
        } else {
            specialParams = new String[] { "pagename", "blobtable" };
        }
    }

}
