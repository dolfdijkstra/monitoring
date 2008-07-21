package com.fatwire.dta.lwa.sensor;

import java.io.File;
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fatwire.dta.lwa.capture.CapturedRequestListener;
import com.fatwire.dta.lwa.capture.RequestCapturer;
import com.fatwire.dta.lwa.capture.minimal.FileCapturedRequestPersister;
import com.fatwire.dta.lwa.capture.minimal.MinimalCapturedRequest;
import com.fatwire.dta.lwa.capture.minimal.MinimalRequestCapturer;

public class RequestCapturingFilter implements Filter {
    private RequestCapturer<MinimalCapturedRequest> capturer;

    private CapturedRequestListener<MinimalCapturedRequest> listener;

    public void destroy() {
        listener.stop();
        capturer.stop();
        capturer = null;
        listener = null;
    }

    public final void doFilter(final ServletRequest request,
            final ServletResponse response, final FilterChain chain)
            throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            doHttpFilter((HttpServletRequest) request,
                    (HttpServletResponse) response, chain);
        } else {
            chain.doFilter(request, response);
        }

    }

    protected void doHttpFilter(final HttpServletRequest request,
            final HttpServletResponse response, final FilterChain chain)
            throws IOException, ServletException {
        final MinimalCapturedRequest capturedRequest = capturer
                .capture(request);
        listener.capturedRequestReceived(capturedRequest);
        chain.doFilter(request, response);

    }

    public void init(final FilterConfig filterConfig) throws ServletException {
        File file = new File(filterConfig.getInitParameter("file"));
        listener = new FileCapturedRequestPersister(file);
        listener.start();
        capturer = new MinimalRequestCapturer();
        capturer.start();

    }

}
