/**
 * 
 */
package com.fatwire.cs.spring.controllers;

import java.lang.management.OperatingSystemMXBean;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.Properties;
import java.util.TreeMap;
import java.util.Map.Entry;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.fatwire.cs.page.HtmlPage;
import com.fatwire.cs.page.PartRenderer;

public class InfoRenderer implements PartRenderer<RequestModel> {

    /**
     * @param out
     */
    public InfoRenderer() {
        super();

    }
    public void renderPart(HtmlPage<RequestModel> page, RequestModel model)
            throws Exception {
        final HttpServletRequest request = model.getRequest();
        final ServletContext context = model.getContext();

        page.printTableOpen();
        printOSInfo(page);
        printVMInfo(page);
        printAddresses(page);
        printContextInfo(page, context);
        printRequestDetails(page, request);
        printRequestParameters(page, request);
        printRequestAttributes(page, request);
        printRequestHeaders(page, request);
        printRequestCookies(page, request);

        printSessionInfo(page, request, request.getSession());

        printContextAttributes(page, context);

        printInitParameters(page, context);

        printSystemProperties(page);

        page.printTableClose();

    }

    @SuppressWarnings("unchecked")
    private void printInitParameters(HtmlPage<RequestModel> page, final ServletContext context) {
        page.printTableSectionTitle("Context init parameters");
        final Enumeration<String> enumeration = context.getInitParameterNames();
        while (enumeration.hasMoreElements()) {
            final String key = enumeration.nextElement();
            final String value = context.getInitParameter(key);
            page.printTableRow(key, value);
        }
    }

    private void printContextInfo(HtmlPage<RequestModel> page, final ServletContext context) {
        page.printTableSectionTitle("Context info");
        page.printTableRow("major version", Integer.toString(context
                .getMajorVersion()));
        page.printTableRow("minor version", Integer.toString(context
                .getMinorVersion()));
        page.printTableRow("ServerInfo", context.getServerInfo());

    }

    @SuppressWarnings("unchecked")
    private void printContextAttributes(HtmlPage<RequestModel> page,
            final ServletContext context) {
        page.printTableSectionTitle("Context attributes");
        final Enumeration<String> enumeration = context.getAttributeNames();

        while (enumeration.hasMoreElements()) {
            final String key = enumeration.nextElement();
            final Object value = context.getAttribute(key);
            page.printTableRow(key, value.toString());
        }
    }

    @SuppressWarnings("unchecked")
    private void printRequestAttributes(HtmlPage<RequestModel> page,
            HttpServletRequest request) {
        page.printTableSectionTitle("Request attributes");
        final Enumeration<String> e = request.getAttributeNames();
        while (e.hasMoreElements()) {
            final String key = e.nextElement();
            Object value = request.getAttribute(key);
            if (value == null) {
                value = "NULL";
            }
            page.printTableRow(key, value.toString());
        }
    }

    private void printSystemProperties(HtmlPage<RequestModel> page) {
        page
                .printTableSectionTitle("<a name=\"SystemP\"></a>System Properties");
        final Properties pSystem = System.getProperties();

        for (final Entry<Object, Object> e : new TreeMap<Object, Object>(
                pSystem).entrySet()) {
            final String sPropertyName = (String) e.getKey();
            final String sPropertyValue = (String) e.getValue();
            page.printTableRow(sPropertyName, sPropertyValue);
        }
    }

    private void printRequestDetails(HtmlPage page,
            final javax.servlet.http.HttpServletRequest request) {

        page.printTableSectionTitle("Servlet Information");
        page.printTableRow("Protocol", request.getProtocol().trim());
        page.printTableRow("Scheme", request.getScheme());
        page.printTableRow("WebServer Name", request.getServerName());
        page.printTableRow("WebServer Port", "" + request.getServerPort());
        page.printTableRow("HTTP Method", request.getMethod());
        page.printTableRow("Remote User", request.getRemoteUser());
        page.printTableRow("Request URI", request.getRequestURI());
        page.printTableRow("Context Path", request.getContextPath());
        page.printTableRow("Servlet Path", request.getServletPath());
        page.printTableRow("Path Info", request.getPathInfo());
        page.printTableRow("Path Trans", request.getPathTranslated());

        page.printTableRow("Query String", request.getQueryString());

        page.printTableRow("WebServer Remote Addr", request.getRemoteAddr());
        page.printTableRow("WebServer Remote Host", request.getRemoteHost());
        page
                .printTableRow("Character Encoding", request
                        .getCharacterEncoding());
        page.printTableRow("Content Length", Integer.toString(request
                .getContentLength()));
        page.printTableRow("Content Type", request.getContentType());
        page.printTableRow("WebServer Locale", request.getLocale().toString());
        page.printTableRow("Request is Secure", Boolean.toString(request
                .isSecure()));
        page.printTableRow("Auth Type", request.getAuthType());
    }

    @SuppressWarnings("unchecked")
    private void printRequestParameters(HtmlPage page,
            final javax.servlet.http.HttpServletRequest request) {
        page.printTableSectionTitle("Parameter names in this request");
        final StringBuilder sbOut = new StringBuilder();
        final Enumeration<String> e2 = request.getParameterNames();
        while (e2.hasMoreElements()) {
            final String key = e2.nextElement();
            final String[] values = request.getParameterValues(key);
            for (int i = 0; i < values.length; i++) {
                if (i > 0) {
                    sbOut.append(",");
                }
                sbOut.append(values[i]);
            }
            page.printTableRow(key, sbOut.toString());
        }
    }

    private void printRequestCookies(HtmlPage page,
            final javax.servlet.http.HttpServletRequest request) {
        page.printTableSectionTitle("Cookies in this request");
        final Cookie[] cookies = request.getCookies();
        if (null != cookies) {
            for (int i = 0; i < cookies.length; i++) {
                final Cookie cookie = cookies[i];
                page.printTableRow(cookie.getName(), cookie.getValue());
            }
        }
    }

    private void printSessionInfo(HtmlPage page,
            final javax.servlet.http.HttpServletRequest request,
            final javax.servlet.http.HttpSession session) {
        final SimpleDateFormat sdf = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss.sss zzz");
        page.printTableSectionTitle("Session information in this model");
        page.printTableRow("Requested Session Id", ""
                + request.getRequestedSessionId());
        page.printTableRow("Current Session Id", "" + session.getId());
        page.printTableRow("Session Created Time", ""
                + sdf.format(new java.util.Date((session.getCreationTime()))));
        page.printTableRow("Session Last Accessed Time", ""
                + sdf
                        .format(new java.util.Date((session
                                .getLastAccessedTime()))));
        page.printTableRow("Session Max Inactive Interval Seconds", ""
                + session.getMaxInactiveInterval());

        page.printTableSectionTitle("Session scoped attributes");
        final Enumeration names = session.getAttributeNames();
        while (names.hasMoreElements()) {
            final String name = (String) names.nextElement();
            page.printTableRow(name, session.getAttribute(name).toString());
        }
    }

    private void printRequestHeaders(HtmlPage page,
            final javax.servlet.http.HttpServletRequest request) {
        page.printTableSectionTitle("<a name=\"RequestH\"></a>Request headers");
        final Enumeration e1 = request.getHeaderNames();
        while (e1.hasMoreElements()) {
            final String key = (String) e1.nextElement();

            for (final Enumeration e2 = request.getHeaders(key); e2
                    .hasMoreElements();) {
                page.printTableRow(key, (String) e2.nextElement());
            }

        }
    }

    private void printOSInfo(HtmlPage page) {
        OperatingSystemMXBean mbean = java.lang.management.ManagementFactory
                .getOperatingSystemMXBean();
        page.printTableSectionTitle("Operation System Information");
        page.printTableRow("architecure", mbean.getArch());
        page.printTableRow("number of processors", Integer.toString(mbean
                .getAvailableProcessors()));
        page.printTableRow("name", mbean.getName());
        page.printTableRow("version", mbean.getVersion());

    }

    private void printVMInfo(HtmlPage page) {
        page.printTableSectionTitle("Java VM Information");
        final Runtime rt = Runtime.getRuntime();
        page.printTableRow("Total Memory", "" + rt.totalMemory() + " bytes");
        page.printTableRow("Free Memory", "" + rt.freeMemory() + " bytes");
    }

    private void printAddresses(HtmlPage page) throws UnknownHostException {
        page.printTableSectionTitle("AppServer DNS Names and IP Addresses", 3);

        final InetAddress local = InetAddress.getLocalHost();
        final InetAddress[] localList = InetAddress.getAllByName(local
                .getHostName());

        for (int i = 0; i < localList.length; i++) {
            final String sHostName = localList[i].getHostName();
            final String sHostIP = localList[i].getHostAddress();
            page.printTableRow(sHostName, sHostIP, localList[i]
                    .getCanonicalHostName());
        }
    }




}
