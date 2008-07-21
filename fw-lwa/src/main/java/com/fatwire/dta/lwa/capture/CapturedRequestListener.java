package com.fatwire.dta.lwa.capture;

public interface CapturedRequestListener<E extends CapturedRequest> {

    void capturedRequestReceived(E capturedRequest) throws PersistanceException;
    
    void start();
    
    void stop();
    
}
