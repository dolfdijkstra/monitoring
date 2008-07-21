/**
 * 
 */
package com.fatwire.dta.lwa.capture.minimal;

import java.io.UnsupportedEncodingException;

import com.fatwire.dta.lwa.capture.RequestEncoder;

final class MinimalCapturedRequestToByteArrayEncoder implements
        RequestEncoder<MinimalCapturedRequest, byte[]> {
    private final MinimalCapturedRequestToStringEncoder toStringEncoder = new MinimalCapturedRequestToStringEncoder();

    public byte[] encode(final MinimalCapturedRequest capturedRequest) {
        try {
            return (toStringEncoder.encode(capturedRequest)+"\r\n").getBytes("UTF-8");
        } catch (final UnsupportedEncodingException e) {
            e.printStackTrace();
            return new byte[0];
        }

    }
}