package com.fatwire.dta.lwa.capture.minimal;

import java.util.Enumeration;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import com.fatwire.dta.lwa.capture.CaptureException;
import com.fatwire.dta.lwa.capture.RequestCapturer;

public class MinimalRequestCapturer implements
        RequestCapturer<MinimalCapturedRequest> {

    public MinimalCapturedRequest capture(final HttpServletRequest request)
            throws CaptureException {
        final MinimalCapturedRequest cap = new MinimalCapturedRequest();
        cap.setUUID(UUID.randomUUID());
        cap.setTimestamp(System.currentTimeMillis());

        cap.setMethod(request.getMethod());
        cap.setProtocol(request.getProtocol());
        cap.setQueryString(request.getQueryString());
        cap.setRemoteAddr(request.getRemoteAddr());
        cap.setRequestedSessionIdValid(request.isRequestedSessionIdValid());
        cap.setRequestedSessionId(request.getRequestedSessionId());
        cap.setRequestURI(request.getRequestURI());
        cap.setRequestURL(request.getRequestURL() == null ? null : request
                .getRequestURL().toString());
        cap.setSecure(request.isSecure());
        cap.setServerName(request.getServerName());
        cap.setServerPort(request.getServerPort());

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
