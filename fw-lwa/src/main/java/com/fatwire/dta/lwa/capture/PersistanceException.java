package com.fatwire.dta.lwa.capture;

import javax.servlet.ServletException;

public class PersistanceException extends ServletException {

    /**
     * 
     */
    private static final long serialVersionUID = 5587124060457179708L;

    public PersistanceException() {
    }

    public PersistanceException(String message) {
        super(message);
    }

    public PersistanceException(Throwable rootCause) {
        super(rootCause);
    }

    public PersistanceException(String message, Throwable rootCause) {
        super(message, rootCause);
    }

}
