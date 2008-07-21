package com.fatwire.dta.lwa.capture.minimal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fatwire.dta.lwa.capture.CapturedRequestListener;
import com.fatwire.dta.lwa.capture.PersistanceException;
import com.fatwire.dta.lwa.capture.RequestEncoder;

public class LoggingCapturedRequestPersister implements
        CapturedRequestListener<MinimalCapturedRequest> {
    private static final Log log = LogFactory
            .getLog(LoggingCapturedRequestPersister.class);

    private static final RequestEncoder<MinimalCapturedRequest, String> encoder = new MinimalCapturedRequestToStringEncoder();

    public void capturedRequestReceived(final MinimalCapturedRequest capturedRequest)
            throws PersistanceException {
        LoggingCapturedRequestPersister.log.info(LoggingCapturedRequestPersister.encoder.encode(capturedRequest));

    }

    public void start() {

    }

    public void stop() {

    }

}
