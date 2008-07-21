package com.fatwire.dta.lwa.capture.full;

import java.util.Enumeration;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import com.fatwire.dta.lwa.capture.CaptureException;
import com.fatwire.dta.lwa.capture.RequestCapturer;

public class FullRequestCapturer implements RequestCapturer<FullCapturedRequest> {

    public FullCapturedRequest capture(HttpServletRequest request) throws CaptureException {
        FullCapturedRequest cap = new FullCapturedRequest();
        cap.setUUID(UUID.randomUUID());
        cap.setTimestamp(System.currentTimeMillis());

        cap.setAuthType(request.getAuthType());
        cap.setCharacterEncoding(request.getCharacterEncoding());
        cap.setContentLength(request.getContentLength());
        cap.setContentType(request.getContentType());
        cap.setContextPath(request.getContextPath());
        cap.setCookies(request.getCookies());
        cap.setLocale(request.getLocale());
        cap.setMethod(request.getMethod());
        cap.setPathInfo(request.getPathInfo());
        cap.setPathTranslated(request.getPathTranslated());
        cap.setProtocol(request.getProtocol());
        cap.setQueryString(request.getQueryString());
        cap.setRemoteAddr(request.getRemoteAddr());
        cap.setRemoteUser(request.getRemoteUser());
        cap.setRequestedSessionIdValid(request.isRequestedSessionIdValid());
        cap.setRequestedSessionId(request.getRequestedSessionId());
        cap.setRequestURI(request.getRequestURI());
        cap.setRequestURL(request.getRequestURL() == null ? null : request
                .getRequestURL().toString());
        cap.setScheme(request.getScheme());
        cap.setSecure(request.isSecure());
        cap.setServerName(request.getServerName());
        cap.setServerPort(request.getServerPort());
        cap.setServletPath(request.getServletPath());
        cap.setUserPrincipal(request.getUserPrincipal());

        for (final Enumeration e = request.getParameterNames(); e
                .hasMoreElements();) {
            final String name = (String) e.nextElement();
            for (final String v : request.getParameterValues(name)) {
                cap.addParameter(name, v);
            }
        }
        for (final Enumeration e = request.getHeaderNames(); e
                .hasMoreElements();) {
            final String name = (String) e.nextElement();
            for (final Enumeration v = request.getHeaders(name); v
                    .hasMoreElements();) {
                final String value = (String) v.nextElement();
                cap.addHeader(name.toLowerCase(), value);
            }
        }
        return cap;

    }

    public void start() {
        
    }

    public void stop() {
        
    }


}
