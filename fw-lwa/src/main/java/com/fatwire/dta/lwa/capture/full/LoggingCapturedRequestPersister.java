package com.fatwire.dta.lwa.capture.full;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fatwire.dta.lwa.capture.CapturedRequestListener;
import com.fatwire.dta.lwa.capture.PersistanceException;

public class LoggingCapturedRequestPersister extends
        AbstractTextCapturedRequestPersister implements
        CapturedRequestListener<FullCapturedRequest> {
    private static final Log log = LogFactory
            .getLog(LoggingCapturedRequestPersister.class);

    public void capturedRequestReceived(FullCapturedRequest capturedRequest)
            throws PersistanceException {
        log.info(serialize(capturedRequest));
    }

}
