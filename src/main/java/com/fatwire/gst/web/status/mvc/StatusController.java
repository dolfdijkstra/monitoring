package com.fatwire.gst.web.status.mvc;

import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.ThreadMXBean;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.mvc.Controller;

import com.fatwire.gst.web.status.RequestCounter;
import com.fatwire.gst.web.status.RequestInfo;
import com.fatwire.gst.web.status.TimeFormat;

public class StatusController implements Controller, ServletContextAware {

    private RequestCounter requestCounter;

    private boolean extendedInfo = false;

    private ServletContext servletContext;

    private final ModelAndView fullTxtView = new ModelAndView(new TxtView(true));

    private final ModelAndView shortTxtView = new ModelAndView(new TxtView(
            false));

    private final ModelAndView htmlView = new ModelAndView(new HtmlView(true));

    class TxtView implements View {
        private final boolean requestInfo;

        /**
         * @param requestInfo
         */
        public TxtView(final boolean requestInfo) {
            super();
            this.requestInfo = requestInfo;
        }

        public void render(final Map arg0, final HttpServletRequest request,
                final HttpServletResponse response) throws Exception {
            response.setContentType("text/plain");

            final PrintWriter writer = response.getWriter();
            writer.println("total:" + requestCounter.getTotalCount());
            writer
                    .println("concurrent:"
                            + requestCounter.getConcurrencyCount());
            if (requestInfo) {
                for (final RequestInfo info : requestCounter
                        .getCurrentExecutingOperations()) {
                    writer.print(format(info));

                }
            }
        }

        protected String format(final RequestInfo info) {
            final StringBuilder b = new StringBuilder();

            b.append(info.getThreadName());
            b.append('\t');
            b.append(info.getCounter());
            b.append('\t');
            b.append(info.isRunning() ? "R" : ".");
            b.append('\t');
            b.append(info.getExecutionTimeForLastRequest());
            b.append('\t');
            b.append(info.getRemoteHost());
            b.append('\t');
            b.append(info.getMethod());
            b.append('\t');
            b.append(info.getCurrentUri());
            b.append('\t');
            b.append(info.getLastStartTime());
            b.append('\t');
            b.append(System.currentTimeMillis() - info.getLastStartTime());
            b.append('\r');
            b.append('\n');
            return b.toString();

        }

        public String getContentType() {
            return "text/plain";
        }

    }

    class HtmlView implements View {
        private final boolean requestInfo;

        private final DateFormat df = new SimpleDateFormat("HH:mm:ss");

        private final TimeFormat tf = new TimeFormat();

        /**
         * @param requestInfo
         */
        public HtmlView(final boolean requestInfo) {
            super();
            this.requestInfo = requestInfo;
        }

        String buildMemReport() {
            final Thread[] list = new Thread[Thread.currentThread()
                    .getThreadGroup().activeGroupCount()];
            Thread.currentThread().getThreadGroup().enumerate(list);
            for (final Thread th : list) {
                th.getState();
            }
            ManagementFactory.getRuntimeMXBean().getUptime();
            final ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
            final MemoryMXBean memBean = ManagementFactory.getMemoryMXBean();
            final List<MemoryPoolMXBean> memPoolBeans = ManagementFactory
                    .getMemoryPoolMXBeans();
            threadBean.getPeakThreadCount();
            threadBean.getThreadCount();
            threadBean.getTotalStartedThreadCount();
            memBean.getHeapMemoryUsage();
            memBean.getNonHeapMemoryUsage();
            memBean.getObjectPendingFinalizationCount();
            for (final MemoryPoolMXBean memPoolBean : memPoolBeans) {
                memPoolBean.getName();
                memPoolBean.getPeakUsage();
                memPoolBean.getCollectionUsage().toString();
            }
            return "";
        }

        /* (non-Javadoc)
         * @see org.springframework.web.servlet.View#getContentType()
         */
        public String getContentType() {
            return "text/html; charset=\"UTF-8\"";
        }

        /* (non-Javadoc)
         * @see org.springframework.web.servlet.View#render(java.util.Map, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
         */
        public void render(final Map map, final HttpServletRequest request,
                final HttpServletResponse response) throws Exception {
            //final long t = System.nanoTime();
            response.setContentType("text/html; charset=\"UTF-8\"");
            final PrintWriter writer = response.getWriter();
            writer
                    .println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
            writer.println("<html><head><title>Status for "
                    + getServletContext().getServerInfo() + " at "
                    + request.getLocalName() + "</title>");
            writer.println("<style type=\"text/css\">");
            writer.println("<!--");
            writer
                    .println("td,th,body { font-size: small; font-family: monospace; }");
            writer.println("tr.requestinfo {white-space: nowrap}");
            //writer.println("tr.running {color: black}");
            writer.println("tr.notrunning {color: #CCCCCC}");
            writer.println("-->");
            writer.println("</style>");
            writer.println("</head><body>");
            writer.print("<h1>Status for "
                    + getServletContext().getServerInfo() + " at "
                    + request.getLocalName() + "</h1>");
            writer.print("<div class=\"summary\">total:"
                    + requestCounter.getTotalCount());
            writer.print("<br/>");
            writer.print("concurrent:" + requestCounter.getConcurrencyCount());
            writer.print("</div>");
            final Collection<RequestInfo> c = requestCounter
                    .getCurrentExecutingOperations();
            //writer.print("<p>" + (System.nanoTime() - t) / 1000 + "us </p>");
            if (requestInfo) {
                writer.print("<table><tr><th>");
                writer.print("thread");
                writer.print("</th><th>");
                writer.print("counter");
                writer.print("</th><th>");
                writer.print("R");
                writer.print("</th><th>");
                writer.print("time");
                writer.print("</th><th>");
                writer.print("remote");
                writer.print("</th><th>");
                writer.print("method");
                writer.print("</th><th>");
                writer.print("uri");
                writer.print("</th><th>");
                writer.print("LST");
                writer.print("</th><th>");
                writer.print("TSLS");
                writer.print("</th><th>");
                writer.print("State");
                writer.print("</th></tr>");
                for (final RequestInfo info : c) {
                    writer.print(format(info));

                }
                writer.print("</table>");
                writer.print("<hr/>");
                writer
                        .print("<table><tr><td>thread<td><td>name of the thread</td></tr>");
                writer
                        .print("<tr><td>counter<td><td>number of invocations by this thread</td></tr>");
                writer
                        .print("<tr><td>R<td><td>is currently running?</td></tr>");
                writer
                        .print("<tr><td>time<td><td>execution time of last request in nana seconds</td></tr>");
                writer
                        .print("<tr><td>remote<td><td>the remote host and port</td></tr>");
                writer.print("<tr><td>method<td><td>the http method</td></tr>");
                writer
                        .print("<tr><td>uri<td><td>short uri of last request</td></tr>");
                writer.print("<tr><td>LST<td><td>Last Start Yime</td></tr>");
                writer
                        .print("<tr><td>TSLS<td><td>time since last start</td></tr>");
                writer
                        .print("<tr><td>State<td><td>The current thread state for this request</td></tr>");

                writer.println("</table>");
            }

            //writer.print("<p>" + (System.nanoTime() - t) / 1000 + "us </p>");
            writer.print("</body></html>");
            writer.flush();
        }

        protected String format(final RequestInfo info) {
            final StringBuilder b = new StringBuilder();
            b.append("<tr class=\"requestinfo "
                    + (info.isRunning() ? "running" : "notrunning") + "\">");
            b.append("<td class=\"threadname\">");
            b.append(info.getThreadName());
            b.append("</td><td class=\"counter\">");
            b.append(info.getCounter());
            b.append("</td><td class=\"running\">");
            b.append(info.isRunning() ? "R" : ".");
            b.append("</td><td class=\"RT\">");
            b.append(tf.format(info.getExecutionTimeForLastRequest()));
            b.append("</td><td class=\"remote_host\">");
            b.append(info.getRemoteHost());
            b.append("</td><td class=\"method\">");
            b.append(info.getMethod());
            b.append("</td><td class=\"uri\">");
            b.append(info.getCurrentUri());
            b.append("</td><td class=\"LST\">");
            b.append(df.format(new Date(info.getLastStartTime())));
            b.append("</td><td class=\"TSLS\">");
            b.append(tf.format(System.currentTimeMillis()
                    - info.getLastStartTime()));
            b.append("</td><td class=\"threadstate\">");
            b.append(info.getThreadState());
            b.append("</td></tr>");

            return b.toString();

        }

    }

    public ModelAndView handleRequest(final HttpServletRequest request,
            final HttpServletResponse response) throws Exception {
        if (request.getParameter("refresh") != null) {
            try {
                final int i = Integer.parseInt(request.getParameter("refresh"));
                if (i > 0) {
                    response.setHeader("refresh", Integer.toString(i));
                }
            } catch (final Throwable e) {
                //ignore
            }
        }

        if (request.getParameter("auto") != null) {
            if (request.getParameter("extendedInfo") != null || extendedInfo) {
                return fullTxtView;
            } else {
                return shortTxtView;
            }
        } else {
            return htmlView;
        }

    }

    /**
     * @return the extendedInfo
     */
    public boolean isExtendedInfo() {
        return extendedInfo;
    }

    /**
     * @param extendedInfo the extendedInfo to set
     */
    public void setExtendedInfo(final boolean extendedInfo) {
        this.extendedInfo = extendedInfo;
    }

    public void setServletContext(final ServletContext context) {
        this.servletContext = context;

    }

    /**
     * @return the servletContext
     */
    public ServletContext getServletContext() {
        return servletContext;
    }

    /**
     * @return the requestCounter
     */
    public RequestCounter getRequestCounter() {
        return requestCounter;
    }

    /**
     * @param requestCounter the requestCounter to set
     */
    public void setRequestCounter(final RequestCounter requestCounter) {
        this.requestCounter = requestCounter;
    }

}
