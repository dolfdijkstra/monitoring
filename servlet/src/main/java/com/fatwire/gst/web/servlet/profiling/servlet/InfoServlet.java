package com.fatwire.gst.web.servlet.profiling.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.Properties;
import java.util.TreeMap;
import java.util.Map.Entry;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import COM.FutureTense.Servlet.SContentServer;

public class InfoServlet extends HttpServlet {

    /**
     * 
     */
    private static final long serialVersionUID = 2427009594776592072L;

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(final HttpServletRequest request,
            final HttpServletResponse response) throws ServletException,
            IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        final PrintWriter out = response.getWriter();
        try {
            new View(out).doView(request, response);
        } catch (final Exception e) {
            throw new ServletException(e.getMessage(), e);
        }
    }

    class Page {

        void doDTD() {

        }

        void doHeader() {

        }

        void doBody() {

        }

        void doEnd() {

        }

    }

    class View {
        private final PrintWriter out;

        /**
         * @param out
         */
        public View(final PrintWriter out) {
            super();
            this.out = out;
        }

        void doView(final HttpServletRequest request,
                final HttpServletResponse response) throws Exception {
            printTableOpen();
            printVMInfo();
            printRequestDetails(request, response);
            printSessionInfo(request, request.getSession());
            printRequestParameters(request);
            printAttributes(request, getServletContext());
            printRequestCookies(request);
            printRequestHeaders(request);
            printSystemProperties();
            printInitParameters(getServletContext());
            printCurrentThreadGroup();
            printAllThreads();
            printClassFolder();
            printAddresses();
            printTableClose();

        }

        private void printTableOpen() throws Exception {
            out.print("<table>");
        }

        private void printTableClose() throws Exception {
            out.print("</table>");
        }

        private void printTableSectionTitle(final String title)
                throws Exception {
            printTableSectionTitle(title, 2);
        }

        private void printTableSectionTitle(final String title, final int span)
                throws Exception {
            out.print("<tr>");
            out.print("<th colspan=\"" + span + "\">");
            out.print(title);
            out.print("</th>");
            out.print("</tr>");
        }

        private void printTableRow(final String... cellValues) throws Exception {
            out.print("<tr>");
            for (final String cell : cellValues) {
                out.print("<td>");
                out.print(cell);
                out.print("</td>");
            }
            out.print("</tr>");
        }

        @SuppressWarnings("unchecked")
        private void printInitParameters(final ServletContext context)
                throws Exception {
            printTableSectionTitle("Context init parameters");
            final Enumeration<String> enum1 = context.getInitParameterNames();
            while (enum1.hasMoreElements()) {
                final String key = enum1.nextElement();
                final String value = context.getInitParameter(key);
                printTableRow(key, value);
            }
        }

        @SuppressWarnings("unchecked")
        private void printAttributes(final ServletRequest request,
                final ServletContext context) throws Exception {
            printTableSectionTitle("Context attributes");
            final Enumeration<String> enum2 = context.getAttributeNames();
            try {
                while (enum2.hasMoreElements()) {
                    final String key = enum2.nextElement();
                    final Object value = context.getAttribute(key);
                    printTableRow(key, value.toString());
                }
            } catch (final Exception e) {
                //do something...
            }

            printTableSectionTitle("Request attributes");
            final Enumeration e = request.getAttributeNames();
            while (e.hasMoreElements()) {
                final String key = (String) e.nextElement();
                Object value = request.getAttribute(key);
                if (value == null) {
                    value = "NULL";
                }
                printTableRow(key, value.toString());
            }
        }

        private void printSystemProperties() throws Exception {
            printTableSectionTitle("<a name=\"SystemP\"></a>System Properties");
            final Properties pSystem = System.getProperties();

            //            final Set en_pNames = new TreeMap<Object,Object>(pSystem).entrySet();
            //            for (final Iterator<Map.Entry<String,String>> itor = en_pNames.iterator(); itor.hasNext();) {
            for (final Entry<Object, Object> e : new TreeMap<Object, Object>(
                    pSystem).entrySet()) {
                final String sPropertyName = (String) e.getKey();
                final String sPropertyValue = (String) e.getValue();
                printTableRow(sPropertyName, sPropertyValue);
            }
        }

        private void printRequestDetails(
                final javax.servlet.http.HttpServletRequest request,
                final javax.servlet.http.HttpServletResponse response)
                throws Exception {
            final ServletConfig config = InfoServlet.this.getServletConfig();
            printTableSectionTitle("Servlet Information");
            printTableRow("Protocol", request.getProtocol().trim());
            printTableRow("Scheme", request.getScheme());
            printTableRow("WebServer Name", request.getServerName());
            printTableRow("WebServer Port", "" + request.getServerPort());
            printTableRow("HTTP Method", request.getMethod());
            printTableRow("Remote User", request.getRemoteUser());
            printTableRow("Request URI", request.getRequestURI());
            printTableRow("Context Path", request.getContextPath());
            printTableRow("Servlet Path", request.getServletPath());
            printTableRow("Path Info", request.getPathInfo());
            printTableRow("Path Trans", request.getPathTranslated());

            printTableRow("Query String", request.getQueryString());
            printTableRow("Servlet Name", config.getServletName());

            printTableRow("WebServer Info", config.getServletContext()
                    .getServerInfo());
            printTableRow("WebServer Remote Addr", request.getRemoteAddr());
            printTableRow("WebServer Remote Host", request.getRemoteHost());
            printTableRow("Character Encoding", request.getCharacterEncoding());
            printTableRow("Content Length", "" + request.getContentLength());
            printTableRow("Content Type", request.getContentType());
            printTableRow("WebServer Locale", request.getLocale().toString());
            printTableRow("Default Response Buffer", ""
                    + response.getBufferSize());
            printTableRow("Request Is Secure", "" + request.isSecure());
            printTableRow("Auth Type", request.getAuthType());
        }

        private void printRequestParameters(
                final javax.servlet.http.HttpServletRequest request)
                throws Exception {
            printTableSectionTitle("Parameter names in this request");
            final StringBuilder sbOut = new StringBuilder();
            final Enumeration e2 = request.getParameterNames();
            while (e2.hasMoreElements()) {
                final String key = (String) e2.nextElement();
                final String[] values = request.getParameterValues(key);
                for (int i = 0; i < values.length; i++) {
                    if (i > 0) {
                        sbOut.append(",");
                    }
                    sbOut.append(values[i]);
                }
                printTableRow(key, sbOut.toString());
            }
        }

        private void printRequestCookies(
                final javax.servlet.http.HttpServletRequest request)
                throws Exception {
            printTableSectionTitle("Cookies in this request");
            final Cookie[] cookies = request.getCookies();
            if (null != cookies) {
                for (int i = 0; i < cookies.length; i++) {
                    final Cookie cookie = cookies[i];
                    printTableRow(cookie.getName(), cookie.getValue());
                }
            }
        }

        @SuppressWarnings("unchecked")
        private void printSessionInfo(
                final javax.servlet.http.HttpServletRequest request,
                final javax.servlet.http.HttpSession session) throws Exception {
            final SimpleDateFormat sdf = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss.sss zzz");
            printTableSectionTitle("Session information in this request");
            printTableRow("Requested Session Id", ""
                    + request.getRequestedSessionId());
            printTableRow("Current Session Id", "" + session.getId());
            printTableRow("Session Created Time", ""
                    + sdf.format(new java.util.Date(session.getCreationTime())));
            printTableRow("Session Last Accessed Time", ""
                    + sdf.format(new java.util.Date(session
                            .getLastAccessedTime())));
            printTableRow("Session Max Inactive Interval Seconds", ""
                    + session.getMaxInactiveInterval());

            printTableSectionTitle("Session scoped attributes");
            final Enumeration<String> names = session.getAttributeNames();
            while (names.hasMoreElements()) {
                final String name = names.nextElement();
                printTableRow(name, session.getAttribute(name).toString());
            }
        }

        @SuppressWarnings("unchecked")
        private void printRequestHeaders(
                final javax.servlet.http.HttpServletRequest request)
                throws Exception {
            printTableSectionTitle("<a name=\"RequestH\"></a>Request headers");
            final Enumeration<String> e1 = request.getHeaderNames();
            while (e1.hasMoreElements()) {
                final String key = e1.nextElement();
                //final Enumeration e2 = request.getHeaders(key);
                final String value = request.getHeader(key);
                printTableRow(key, value);
            }
        }

        private void printVMInfo() throws Exception {
            printTableSectionTitle("Java VM Information");
            final Runtime rt = Runtime.getRuntime();
            //printTableRow("Max Memory", "" + rt.maxMemory() + " bytes"); 
            printTableRow("Total Memory", "" + rt.totalMemory() + " bytes");
            printTableRow("Free Memory", "" + rt.freeMemory() + " bytes");
        }

        private void printCurrentDate() throws Exception {
            final SimpleDateFormat sdf = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss.sss zzz");
            printTableSectionTitle("Current Date");
            printTableRow("Date", sdf.format(new java.util.Date()));
        }

        private void printAddresses() throws Exception {
            printTableSectionTitle("AppServer DNS Names and IP Addresses", 3);

            final InetAddress local = InetAddress.getLocalHost();
            final InetAddress[] localList = InetAddress.getAllByName(local
                    .getHostName());

            for (int i = 0; i < localList.length; i++) {
                final String sHostName = localList[i].getHostName();
                final String sHostIP = localList[i].getHostAddress();
                printTableRow(sHostName, sHostIP, localList[i]
                        .getCanonicalHostName());
            }
        }

        private void printClassFolder() throws Exception {
            printTableSectionTitle("ContentServer deployment folder");
            final Class<SContentServer> csClass = COM.FutureTense.Servlet.SContentServer.class;
            printTableRow("SContentServer loaded from", csClass.getResource(
                    "SContentServer.class").toString());
        }

        private void printCurrentThreadGroup() throws Exception {
            printTableSectionTitle("<a name=\"Threads\"></a>Threads in the current thread group");

            final Thread current = Thread.currentThread();
            printTableRow("Current Thread", current.toString());
            final ThreadGroup tgCurrent = current.getThreadGroup();
            // double the current active count to be very safe
            final int sizeEstimate = tgCurrent.activeCount() * 2;
            final Thread[] threadList = new Thread[sizeEstimate];
            final int size = tgCurrent.enumerate(threadList);
            for (int i = 0; i < size; i++) {
                printTableRow("Thread" + i, threadList[i].toString());
            }
        }

        private void printAllThreads() throws Exception {
            printTableSectionTitle("All the threads in the java VM");
            ThreadGroup group = Thread.currentThread().getThreadGroup();
            ThreadGroup rootGroup = null;

            // traverse the tree to the root group
            while (group != null) {
                rootGroup = group;
                group = group.getParent();
            }

            // double the current active count to be very safe
            final int sizeEstimate = rootGroup.activeCount() * 2;
            final Thread[] threadList1 = new Thread[sizeEstimate];

            final int size = rootGroup.enumerate(threadList1);

            for (int i = 0; i < size; i++) {
                printTableRow("Thread" + i, threadList1[i].toString());
            }
        }
    }
}
