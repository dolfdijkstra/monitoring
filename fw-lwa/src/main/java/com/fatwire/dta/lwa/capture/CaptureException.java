package com.fatwire.dta.lwa.capture;

import javax.servlet.ServletException;

public class CaptureException extends ServletException {

    /**
     * 
     */
    private static final long serialVersionUID = 1522832126341844066L;

    public CaptureException() {
    }

    public CaptureException(String message) {
        super(message);
    }

    public CaptureException(Throwable rootCause) {
        super(rootCause);
    }

    public CaptureException(String message, Throwable rootCause) {
        super(message, rootCause);
    }

}
