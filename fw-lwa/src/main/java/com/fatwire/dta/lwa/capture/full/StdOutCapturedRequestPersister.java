package com.fatwire.dta.lwa.capture.full;

import com.fatwire.dta.lwa.capture.CapturedRequestListener;
import com.fatwire.dta.lwa.capture.PersistanceException;

public class StdOutCapturedRequestPersister extends AbstractTextCapturedRequestPersister implements
        CapturedRequestListener<FullCapturedRequest> {

    public void capturedRequestReceived(FullCapturedRequest capturedRequest)
            throws PersistanceException {
        System.out.println(serialize(capturedRequest));

    }

}
