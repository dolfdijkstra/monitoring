/**
 * 
 */
package com.fatwire.dta.lwa.capture.minimal;

import com.fatwire.dta.lwa.capture.NameValuePair;
import com.fatwire.dta.lwa.capture.RequestEncoder;

final class MinimalCapturedRequestToStringEncoder implements
        RequestEncoder<MinimalCapturedRequest, String> {

    public String encode(final MinimalCapturedRequest capturedRequest) {
        final StringBuilder b = new StringBuilder();
        b.append(capturedRequest.getProtocol());
        b.append(' ');

        b.append(capturedRequest.getRequestURL());
        if (capturedRequest.getQueryString() != null) {
            b.append('?');
            b.append(capturedRequest.getQueryString());
        }
        for (final NameValuePair header : capturedRequest.getHeaders()) {
            b.append(' ');
            b.append(header.toString());
        }

        return b.toString();

    }
}