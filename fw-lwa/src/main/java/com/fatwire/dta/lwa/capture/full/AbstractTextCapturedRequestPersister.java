package com.fatwire.dta.lwa.capture.full;

import com.fatwire.dta.lwa.capture.NameValuePair;

public class AbstractTextCapturedRequestPersister {

    public AbstractTextCapturedRequestPersister() {
        super();
    }

    protected String serialize(FullCapturedRequest capturedRequest) {
        final StringBuilder b = new StringBuilder();
        b.append(capturedRequest.getProtocol());
        b.append(' ');

        b.append(capturedRequest.getRequestURL());
        if (capturedRequest.getQueryString() != null) {
            b.append('?');
            b.append(capturedRequest.getQueryString());
        }
        for (NameValuePair header : capturedRequest.getHeaders()) {
            b.append(' ');
            b.append(header.toString());
        }
        return b.toString();
    }

    public void start() {
    }

    public void stop() {
    }

}